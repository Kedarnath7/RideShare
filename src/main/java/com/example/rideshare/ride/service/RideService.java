package com.example.rideshare.ride.service;

import com.example.rideshare.booking.entity.Booking;
import com.example.rideshare.booking.repository.BookingRepository;
import com.example.rideshare.notification.service.NotificationService;
import com.example.rideshare.ride.dto.RideDto;
import com.example.rideshare.ride.entity.Ride;
import com.example.rideshare.ride.entity.RideStatus;
import com.example.rideshare.ride.repository.RideRepository;
import com.example.rideshare.user.entity.User;
import com.example.rideshare.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    // Dependencies for Notification Logic
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    public RideService(RideRepository rideRepository,
                       UserRepository userRepository,
                       BookingRepository bookingRepository,
                       NotificationService notificationService) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
    }

    // --- 1. CORE CRUD OPERATIONS ---

    public RideDto createRide(Ride ride, Long driverId) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        ride.setDriver(driver);
        ride.setStatus(RideStatus.CREATED);

        Ride savedRide = rideRepository.save(ride);
        return mapToDto(savedRide);
    }

    public List<RideDto> searchRides(String source, String destination) {
        List<Ride> rides = rideRepository.findBySourceAndDestination(source, destination);
        return rides.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<RideDto> getAllRides() {
        return rideRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    // --- 2. LIFECYCLE & NOTIFICATIONS ---

    // A. Driver Starts Ride -> Notify Passengers
    @Transactional
    public RideDto startRide(Long rideId, Long driverId) {
        Ride ride = getRideAndVerifyDriver(rideId, driverId);

        if (ride.getStatus() != RideStatus.CREATED && ride.getStatus() != RideStatus.BOOKED) {
            throw new RuntimeException("Ride cannot be started. Current status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.BOOKED); // Using BOOKED or IN_PROGRESS
        Ride savedRide = rideRepository.save(ride);

        notifyAllPassengers(rideId, "🚗 Your ride to " + ride.getDestination() + " has started! Please buckle up.");
        return mapToDto(savedRide);
    }

    // B. Driver Completes Ride -> Notify Passengers
    @Transactional
    public RideDto completeRide(Long rideId, Long driverId) {
        Ride ride = getRideAndVerifyDriver(rideId, driverId);

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new RuntimeException("Ride is already completed.");
        }

        ride.setStatus(RideStatus.COMPLETED);
        Ride savedRide = rideRepository.save(ride);

        notifyAllPassengers(rideId, "✅ Destination reached! Thank you for riding. Please check your payment status.");
        return mapToDto(savedRide);
    }

    // C. Driver Cancels Ride -> Notify Passengers (URGENT)
    @Transactional
    public RideDto cancelRide(Long rideId, Long driverId) {
        Ride ride = getRideAndVerifyDriver(rideId, driverId);

        if (ride.getStatus() == RideStatus.COMPLETED || ride.getStatus() == RideStatus.CANCELLED) {
            throw new RuntimeException("Ride is already processed");
        }

        ride.setStatus(RideStatus.CANCELLED);
        Ride savedRide = rideRepository.save(ride);

        notifyAllPassengers(rideId, "⚠️ URGENT: Your ride to " + ride.getDestination() + " was CANCELLED by the driver.");
        return mapToDto(savedRide);
    }

    // --- HELPER METHODS ---

    private Ride getRideAndVerifyDriver(Long rideId, Long driverId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (!ride.getDriver().getId().equals(driverId)) {
            throw new RuntimeException("You are not authorized to perform this action");
        }
        return ride;
    }

    private void notifyAllPassengers(Long rideId, String message) {
        List<Booking> bookings = bookingRepository.findByRideId(rideId);
        for (Booking booking : bookings) {
            notificationService.sendNotification(booking.getPassenger(), message);
        }
    }

    private RideDto mapToDto(Ride ride) {
        return new RideDto(
                ride.getId(),
                ride.getSource(),
                ride.getDestination(),
                ride.getDepartureTime(),
                ride.getPricePerSeat(),
                ride.getAvailableSeats(),
                ride.getCarModel(),
                ride.getStatus(),
                ride.getDriver().getId(),
                ride.getDriver().getFullName()
        );
    }
}