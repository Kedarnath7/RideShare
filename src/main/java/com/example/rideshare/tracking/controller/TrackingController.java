//package com.example.rideshare.tracking.controller;
//
//import com.example.rideshare.common.ApiResponse;
//import com.example.rideshare.tracking.dto.LocationDto;
//import com.example.rideshare.tracking.service.TrackingService;
//import lombok.Data;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/tracking")
//public class TrackingController {
//
//    private final TrackingService trackingService;
//
//    public TrackingController(TrackingService trackingService) {
//        this.trackingService = trackingService;
//    }
//
//    // PUT /api/tracking/{rideId}
//    // Driver sends updates here
//    @PutMapping("/{rideId}")
//    public ResponseEntity<ApiResponse<String>> updateDriverLocation(
//            @PathVariable Long rideId,
//            @RequestBody LocationUpdateRequest request) {
//
//        trackingService.updateLocation(rideId, request.getLatitude(), request.getLongitude());
//        return ResponseEntity.ok(ApiResponse.success("Location updated", null));
//    }
//
//    // GET /api/tracking/{rideId}
//    // Passenger polls this endpoint every few seconds
//    @GetMapping("/{rideId}")
//    public ResponseEntity<ApiResponse<LocationDto>> getLiveLocation(@PathVariable Long rideId) {
//        try {
//            LocationDto location = trackingService.getRideLocation(rideId);
//            return ResponseEntity.ok(ApiResponse.success("Location found", location));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(ApiResponse.error(e.getMessage()));
//        }
//    }
//
//    @Data
//    public static class LocationUpdateRequest {
//        private Double latitude;
//        private Double longitude;
//    }
//}
package com.example.rideshare.tracking.controller;

import com.example.rideshare.common.ApiResponse;
import com.example.rideshare.tracking.dto.LocationDto;
import com.example.rideshare.tracking.service.TrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
@Tag(name = "Live Tracking", description = "Geospatial services for updating and fetching ride location")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @Operation(summary = "Update Driver Location", description = "Driver app sends GPS coordinates here every few seconds.")
    @PutMapping("/{rideId}")
    public ResponseEntity<ApiResponse<String>> updateDriverLocation(
            @PathVariable Long rideId,
            @RequestBody LocationUpdateRequest request) {

        trackingService.updateLocation(rideId, request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(ApiResponse.success("Location updated", null));
    }

    @Operation(summary = "Get Live Location", description = "Passengers poll this endpoint to see where the car is on the map.")
    @GetMapping("/{rideId}")
    public ResponseEntity<ApiResponse<LocationDto>> getLiveLocation(@PathVariable Long rideId) {
        try {
            LocationDto location = trackingService.getRideLocation(rideId);
            return ResponseEntity.ok(ApiResponse.success("Location found", location));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Data
    public static class LocationUpdateRequest {
        private Double latitude;
        private Double longitude;
    }
}