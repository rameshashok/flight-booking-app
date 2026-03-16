package com.example.flightbooking.service;

import com.example.flightbooking.model.Flight;
import com.example.flightbooking.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final AviaStackService aviaStackService;

    public List<Flight> searchFlights(String depIata, String arrIata, LocalDate date) {
        return aviaStackService.searchFlights(depIata, arrIata, date);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
}
