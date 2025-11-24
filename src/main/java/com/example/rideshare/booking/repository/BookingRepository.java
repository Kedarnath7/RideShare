package com.example.rideshare.booking.repository;

import com.example.rideshare.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings made by a specific passenger (My Trips)
    List<Booking> findByPassengerId(Long passengerId);

    // NEW: Find all bookings for a specific ride (For notifying passengers)
    List<Booking> findByRideId(Long rideId);
}