package com.example.rideshare.ride.entity;

import com.example.rideshare.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private Double pricePerSeat;

    @Column(nullable = false)
    private Integer availableSeats;

    @Column(nullable = false)
    private String carModel;

    @Enumerated(EnumType.STRING)
    private RideStatus status = RideStatus.CREATED;

    // RELATIONSHIP: Many Rides can belong to One Driver
    @ManyToOne(fetch = FetchType.EAGER) // Load driver details when we load a ride
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;
}