package com.example.rideshare.ride.dto;

import com.example.rideshare.ride.entity.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideDto {
    private Long id;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private Double pricePerSeat;
    private Integer availableSeats;
    private String carModel;
    private RideStatus status;

    // Driver Info (Flattened for easier frontend consumption)
    private Long driverId;
    private String driverName;
}