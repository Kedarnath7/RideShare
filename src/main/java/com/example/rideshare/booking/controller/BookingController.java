//package com.example.rideshare.booking.controller;
//
//import com.example.rideshare.booking.dto.BookingDto;
//import com.example.rideshare.booking.service.BookingService;
//import com.example.rideshare.common.ApiResponse;
//import lombok.Data;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/bookings")
//public class BookingController {
//
//    private final BookingService bookingService;
//
//    public BookingController(BookingService bookingService) {
//        this.bookingService = bookingService;
//    }
//
//    // POST /api/bookings?passengerId=1
//    @PostMapping
//    public ResponseEntity<ApiResponse<BookingDto>> bookRide(
//            @RequestParam Long passengerId,
//            @RequestBody BookingRequest request) {
//        try {
//            BookingDto booking = bookingService.createBooking(
//                    request.getRideId(),
//                    passengerId,
//                    request.getSeats()
//            );
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(ApiResponse.success("Ride booked successfully!", booking));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(ApiResponse.error(e.getMessage()));
//        }
//    }
//
//    // GET /api/bookings/my-bookings?passengerId=1
//    @GetMapping("/my-bookings")
//    public ResponseEntity<ApiResponse<List<BookingDto>>> getMyBookings(@RequestParam Long passengerId) {
//        List<BookingDto> bookings = bookingService.getBookingsByPassenger(passengerId);
//        return ResponseEntity.ok(ApiResponse.success("User bookings retrieved", bookings));
//    }
//
//    // Simple Request DTO
//    @Data
//    public static class BookingRequest {
//        private Long rideId;
//        private Integer seats;
//    }
//}
package com.example.rideshare.booking.controller;

import com.example.rideshare.booking.dto.BookingDto;
import com.example.rideshare.booking.service.BookingService;
import com.example.rideshare.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Booking System", description = "Manage seat reservations and booking history")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Book a Seat", description = "Reserves a specific number of seats on a ride. Decreases available seat count.")
    @PostMapping
    public ResponseEntity<ApiResponse<BookingDto>> bookRide(
            @RequestParam Long passengerId,
            @RequestBody BookingRequest request) {
        try {
            BookingDto booking = bookingService.createBooking(
                    request.getRideId(),
                    passengerId,
                    request.getSeats()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Ride booked successfully!", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Get My Bookings", description = "Retrieve all booking history for a specific passenger.")
    @GetMapping("/my-bookings")
    public ResponseEntity<ApiResponse<List<BookingDto>>> getMyBookings(@RequestParam Long passengerId) {
        List<BookingDto> bookings = bookingService.getBookingsByPassenger(passengerId);
        return ResponseEntity.ok(ApiResponse.success("User bookings retrieved", bookings));
    }

    // Simple Request DTO
    @Data
    public static class BookingRequest {
        private Long rideId;
        private Integer seats;
    }
}