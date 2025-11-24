package com.example.rideshare.ride.entity;

public enum RideStatus {
    CREATED,    // Driver posted it
    BOOKED,     // Fully booked (optional state)
    COMPLETED,  // Ride finished
    CANCELLED   // Driver cancelled
}