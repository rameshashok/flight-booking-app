package com.example.flightbooking.controller;

import com.example.flightbooking.model.Booking;
import com.example.flightbooking.model.Flight;
import com.example.flightbooking.service.BookingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @Data
    static class BookingRequest {
        private Long flightId;
        private String flightNumber;
        private String origin;
        private String destination;
        private LocalDateTime departureTime;
        private LocalDateTime arrivalTime;
        private BigDecimal price;
        private int availableSeats;
        private String passengerName;
        private String passengerEmail;
        private int seats;

        Flight toFlight() {
            Flight f = new Flight();
            f.setId(flightId);
            f.setFlightNumber(flightNumber);
            f.setOrigin(origin);
            f.setDestination(destination);
            f.setDepartureTime(departureTime);
            f.setArrivalTime(arrivalTime);
            f.setPrice(price);
            f.setAvailableSeats(availableSeats);
            return f;
        }
    }

    @PostMapping
    public Booking createBooking(@RequestBody BookingRequest req) {
        return bookingService.createBooking(req.toFlight(), req.getPassengerName(), req.getPassengerEmail(), req.getSeats());
    }

    @GetMapping
    public List<Booking> getByEmail(@RequestParam String email) {
        return bookingService.getBookingsByEmail(email);
    }

    @PutMapping("/{id}/cancel")
    public Booking cancel(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }
}
