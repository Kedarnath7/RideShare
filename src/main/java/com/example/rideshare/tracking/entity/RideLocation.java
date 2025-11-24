package com.example.rideshare.tracking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ride_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We store ID instead of the full Ride object to keep it lightweight
    // This is a loose coupling strategy
    @Column(nullable = false)
    private Long rideId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private LocalDateTime timestamp;
}