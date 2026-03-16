package com.example.flightbooking.controller;

import com.example.flightbooking.model.Booking;
import com.example.flightbooking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestBody Map<String, Object> body) {
        Long flightId = Long.valueOf(body.get("flightId").toString());
        String name = body.get("passengerName").toString();
        String email = body.get("passengerEmail").toString();
        int seats = Integer.parseInt(body.get("seats").toString());
        return bookingService.createBooking(flightId, name, email, seats);
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
