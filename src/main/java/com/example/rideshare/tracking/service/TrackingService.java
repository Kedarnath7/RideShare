package com.example.rideshare.tracking.service;

import com.example.rideshare.tracking.dto.LocationDto;
import com.example.rideshare.tracking.entity.RideLocation;
import com.example.rideshare.tracking.repository.TrackingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TrackingService {

    private final TrackingRepository trackingRepository;

    public TrackingService(TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    // 1. Driver updates location
    public void updateLocation(Long rideId, Double lat, Double lng) {
        RideLocation location = new RideLocation();
        location.setRideId(rideId);
        location.setLatitude(lat);
        location.setLongitude(lng);
        location.setTimestamp(LocalDateTime.now());

        trackingRepository.save(location);
    }

    // 2. Passenger gets latest location
    public LocationDto getRideLocation(Long rideId) {
        RideLocation location = trackingRepository.findTopByRideIdOrderByTimestampDesc(rideId)
                .orElseThrow(() -> new RuntimeException("No location data found for this ride"));

        return new LocationDto(
                location.getLatitude(),
                location.getLongitude(),
                location.getTimestamp()
        );
    }
}