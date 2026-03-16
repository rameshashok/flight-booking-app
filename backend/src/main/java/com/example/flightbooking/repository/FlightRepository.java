package com.example.flightbooking.repository;

import com.example.flightbooking.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByOriginIgnoreCaseAndDestinationIgnoreCaseAndDepartureTimeBetween(
        String origin, String destination, LocalDateTime start, LocalDateTime end);
}
