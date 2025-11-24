package com.example.rideshare.tracking.repository;

import com.example.rideshare.tracking.entity.RideLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackingRepository extends JpaRepository<RideLocation, Long> {

    // Get the most recent location update for a ride
    // "Top 1" + "OrderBy Desc" gives us the latest entry
    Optional<RideLocation> findTopByRideIdOrderByTimestampDesc(Long rideId);
}