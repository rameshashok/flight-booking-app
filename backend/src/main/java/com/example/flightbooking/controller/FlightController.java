package com.example.flightbooking.controller;

import com.example.flightbooking.model.Flight;
import com.example.flightbooking.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @GetMapping
    public List<Flight> getAll() {
        return flightService.getAllFlights();
    }

    @GetMapping("/search")
    public List<Flight> search(
            @RequestParam String depIata,
            @RequestParam String arrIata) {
        return flightService.searchFlights(depIata, arrIata);
    }
}
