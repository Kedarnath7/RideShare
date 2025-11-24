package com.example.rideshare.tracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
}