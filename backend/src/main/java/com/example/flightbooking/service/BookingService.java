package com.example.flightbooking.service;

import com.example.flightbooking.model.Booking;
import com.example.flightbooking.model.Flight;
import com.example.flightbooking.repository.BookingRepository;
import com.example.flightbooking.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    @Transactional
    public Booking createBooking(Flight flightData, String name, String email, int seats) {
        // Upsert: persist the flight if it doesn't exist in DB yet (AviaStack flights are transient)
        Flight flight = flightRepository.findById(flightData.getId())
                .orElseGet(() -> flightRepository.save(flightData));

        if (flight.getAvailableSeats() < seats) {
            throw new RuntimeException("Not enough seats available");
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - seats);
        flightRepository.save(flight);

        Booking booking = new Booking();
        booking.setFlight(flight);
        booking.setPassengerName(name);
        booking.setPassengerEmail(email);
        booking.setSeats(seats);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByPassengerEmailIgnoreCase(email);
    }

    @Transactional
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.getFlight().setAvailableSeats(booking.getFlight().getAvailableSeats() + booking.getSeats());
        flightRepository.save(booking.getFlight());
        return bookingRepository.save(booking);
    }
}
