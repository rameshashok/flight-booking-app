package com.example.flightbooking.service;

import com.example.flightbooking.model.Flight;
import com.example.flightbooking.repository.FlightRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public List<Flight> searchFlights(String origin, String destination, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return flightRepository.findByOriginIgnoreCaseAndDestinationIgnoreCaseAndDepartureTimeBetween(
                origin, destination, start, end);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @PostConstruct
    public void seedData() {
        if (flightRepository.count() > 0) return;

        LocalDateTime base = LocalDateTime.now().plusDays(1).withHour(8).withMinute(0).withSecond(0);
        flightRepository.saveAll(List.of(
            flight("AA101", "New York", "Los Angeles", base, base.plusHours(6), new BigDecimal("299.99"), 50),
            flight("AA102", "Los Angeles", "New York", base.plusHours(2), base.plusHours(8), new BigDecimal("319.99"), 45),
            flight("UA201", "New York", "Chicago", base, base.plusHours(2), new BigDecimal("149.99"), 80),
            flight("UA202", "Chicago", "New York", base.plusHours(3), base.plusHours(5), new BigDecimal("159.99"), 70),
            flight("DL301", "New York", "Miami", base.plusHours(1), base.plusHours(4), new BigDecimal("199.99"), 60),
            flight("DL302", "Miami", "New York", base.plusHours(5), base.plusHours(8), new BigDecimal("209.99"), 55),
            flight("SW401", "Chicago", "Los Angeles", base, base.plusHours(4), new BigDecimal("249.99"), 90),
            flight("SW402", "Los Angeles", "Chicago", base.plusHours(6), base.plusHours(10), new BigDecimal("239.99"), 85)
        ));
    }

    private Flight flight(String number, String origin, String dest,
                          LocalDateTime dep, LocalDateTime arr, BigDecimal price, int seats) {
        Flight f = new Flight();
        f.setFlightNumber(number);
        f.setOrigin(origin);
        f.setDestination(dest);
        f.setDepartureTime(dep);
        f.setArrivalTime(arr);
        f.setPrice(price);
        f.setAvailableSeats(seats);
        return f;
    }
}
