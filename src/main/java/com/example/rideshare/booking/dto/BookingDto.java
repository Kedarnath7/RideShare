package com.example.rideshare.booking.dto;

import com.example.rideshare.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime bookingTime;
    private BookingStatus status;
    private Integer seatsBooked;

    // Ride Summary (So the passenger knows what they booked)
    private Long rideId;
    private String source;
    private String destination;
    private LocalDateTime departureTime;

    // Passenger Info
    private Long passengerId;
    private String passengerName;
}