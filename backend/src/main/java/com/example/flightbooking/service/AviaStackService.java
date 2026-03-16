package com.example.flightbooking.service;

import com.example.flightbooking.model.Flight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AviaStackService {

    private final WebClient webClient;
    private final String apiKey;
    private final AtomicLong idSequence = new AtomicLong(1000);

    public AviaStackService(
            @Value("${aviastack.base-url}") String baseUrl,
            @Value("${aviastack.api-key}") String apiKey) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
    }

    public List<Flight> searchFlights(String depIata, String arrIata, LocalDate date) {
        try {
            AviaStackResponse response = webClient.get()
                    .uri(uri -> uri.path("/flights")
                            .queryParam("access_key", apiKey)
                            .queryParam("dep_iata", depIata.toUpperCase())
                            .queryParam("arr_iata", arrIata.toUpperCase())
                            .queryParam("flight_date", date.toString())
                            .build())
                    .retrieve()
                    .bodyToMono(AviaStackResponse.class)
                    .block();

            if (response == null || response.getData() == null) return Collections.emptyList();

            return response.getData().stream()
                    .filter(f -> f.getFlight() != null && f.getDeparture() != null && f.getArrival() != null)
                    .filter(f -> !"cancelled".equalsIgnoreCase(f.getFlightStatus()))
                    .map(this::toFlight)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("AviaStack API error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    private Flight toFlight(AviaStackResponse.AviaStackFlight src) {
        Flight f = new Flight();
        f.setId(idSequence.getAndIncrement());
        f.setFlightNumber(src.getFlight().getIata() != null ? src.getFlight().getIata() : src.getFlight().getNumber());
        f.setOrigin(src.getDeparture().getAirport() != null ? src.getDeparture().getAirport() : src.getDeparture().getIata());
        f.setDestination(src.getArrival().getAirport() != null ? src.getArrival().getAirport() : src.getArrival().getIata());
        f.setDepartureTime(parseDateTime(src.getDeparture().getScheduled()));
        f.setArrivalTime(parseDateTime(src.getArrival().getScheduled()));
        f.setPrice(BigDecimal.valueOf(99 + (Math.abs(f.getFlightNumber().hashCode()) % 400)));
        f.setAvailableSeats(10 + (Math.abs(f.getFlightNumber().hashCode()) % 140));
        return f;
    }

    private LocalDateTime parseDateTime(String iso) {
        if (iso == null) return null;
        try {
            return LocalDateTime.parse(iso, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.parse(iso, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }
}
