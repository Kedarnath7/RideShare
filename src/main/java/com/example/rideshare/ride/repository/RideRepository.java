package com.example.rideshare.ride.repository;

import com.example.rideshare.ride.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    // Custom Query: Find rides by route
    // Spring Data JPA translates this to:
    // SELECT * FROM rides WHERE source = ? AND destination = ?
    List<Ride> findBySourceAndDestination(String source, String destination);
}