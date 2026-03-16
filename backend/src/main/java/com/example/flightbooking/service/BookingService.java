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
    public Booking createBooking(Long flightId, String name, String email, int seats) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

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
