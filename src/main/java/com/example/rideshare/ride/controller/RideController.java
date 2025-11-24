//package com.example.rideshare.ride.controller;
//
//import com.example.rideshare.common.ApiResponse;
//import com.example.rideshare.ride.dto.RideDto;
//import com.example.rideshare.ride.entity.Ride;
//import com.example.rideshare.ride.service.RideService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/rides")
//public class RideController {
//
//    private final RideService rideService;
//
//    public RideController(RideService rideService) {
//        this.rideService = rideService;
//    }
//
//    // POST /api/rides?driverId={id}
//    @PostMapping
//    public ResponseEntity<ApiResponse<RideDto>> createRide(
//            @RequestBody Ride ride,
//            @RequestParam Long driverId) {
//        try {
//            RideDto createdRide = rideService.createRide(ride, driverId);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(ApiResponse.success("Ride posted successfully", createdRide));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(ApiResponse.error(e.getMessage()));
//        }
//    }
//
//    // GET /api/rides/search?source=CityA&destination=CityB
//    @GetMapping("/search")
//    public ResponseEntity<ApiResponse<List<RideDto>>> searchRides(
//            @RequestParam String source,
//            @RequestParam String destination) {
//        List<RideDto> rides = rideService.searchRides(source, destination);
//        return ResponseEntity.ok(ApiResponse.success("Rides found", rides));
//    }
//}
package com.example.rideshare.ride.controller;

import com.example.rideshare.common.ApiResponse;
import com.example.rideshare.ride.dto.RideDto;
import com.example.rideshare.ride.entity.Ride;
import com.example.rideshare.ride.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@Tag(name = "Ride Management", description = "Endpoints for Drivers to post rides and Passengers to search for them")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @Operation(summary = "Post a new Ride", description = "Allows a driver to publish a new ride schedule. Returns the created ride details.")
    @PostMapping
    public ResponseEntity<ApiResponse<RideDto>> createRide(
            @RequestBody Ride ride,
            @RequestParam Long driverId) {
        try {
            RideDto createdRide = rideService.createRide(ride, driverId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Ride posted successfully", createdRide));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Search for Rides", description = "Find available rides matching a source and destination city.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RideDto>>> searchRides(
            @RequestParam String source,
            @RequestParam String destination) {
        List<RideDto> rides = rideService.searchRides(source, destination);
        return ResponseEntity.ok(ApiResponse.success("Rides found", rides));
    }
}