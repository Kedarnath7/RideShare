package com.example.rideshare.booking.service;

import com.example.rideshare.booking.dto.BookingDto;
import com.example.rideshare.booking.entity.Booking;
import com.example.rideshare.booking.entity.BookingStatus;
import com.example.rideshare.booking.repository.BookingRepository;
import com.example.rideshare.notification.service.NotificationService;
import com.example.rideshare.ride.entity.Ride;
import com.example.rideshare.ride.repository.RideRepository;
import com.example.rideshare.user.entity.User;
import com.example.rideshare.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public BookingService(BookingRepository bookingRepository,
                          RideRepository rideRepository,
                          UserRepository userRepository,
                          NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    // 1. Create Booking -> Notify Passenger & Driver
    @Transactional
    public BookingDto createBooking(Long rideId, Long passengerId, Integer seats) {
        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found"));
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getAvailableSeats() < seats) {
            throw new RuntimeException("Not enough seats available.");
        }

        Booking booking = new Booking();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        booking.setSeatsBooked(seats);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.BOOKED);

        ride.setAvailableSeats(ride.getAvailableSeats() - seats);
        rideRepository.save(ride);

        Booking savedBooking = bookingRepository.save(booking);

        // NOTIFICATION 1: To Passenger
        notificationService.sendNotification(
                passenger,
                "Booking Confirmed! You have booked " + seats + " seat(s) for ride to " + ride.getDestination()
        );

        // NOTIFICATION 2: To Driver
        notificationService.sendNotification(
                ride.getDriver(),
                "New Booking! " + passenger.getFullName() + " booked " + seats + " seat(s) on your ride."
        );

        return mapToDto(savedBooking);
    }

    // 2. Cancel Booking -> Notify Driver
    @Transactional
    public void cancelBooking(Long bookingId, Long passengerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getPassenger().getId().equals(passengerId)) {
            throw new RuntimeException("You can only cancel your own bookings");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        Ride ride = booking.getRide();
        ride.setAvailableSeats(ride.getAvailableSeats() + booking.getSeatsBooked());
        rideRepository.save(ride);

        // NOTIFICATION: To Driver
        notificationService.sendNotification(
                ride.getDriver(),
                "Update: A passenger cancelled. You have " + booking.getSeatsBooked() + " seat(s) back."
        );

        // NOTIFICATION: To Passenger (Confirmation)
        notificationService.sendNotification(
                booking.getPassenger(),
                "Booking cancelled successfully."
        );
    }

    public List<BookingDto> getBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private BookingDto mapToDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getBookingTime(),
                booking.getStatus(),
                booking.getSeatsBooked(),
                booking.getRide().getId(),
                booking.getRide().getSource(),
                booking.getRide().getDestination(),
                booking.getRide().getDepartureTime(),
                booking.getPassenger().getId(),
                booking.getPassenger().getFullName()
        );
    }
}