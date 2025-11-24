//package com.example.rideshare.notification.controller;
//
//import com.example.rideshare.common.ApiResponse;
//import com.example.rideshare.notification.dto.NotificationDto;
//import com.example.rideshare.notification.service.NotificationService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/notifications")
//public class NotificationController {
//
//    private final NotificationService notificationService;
//    private final com.example.rideshare.user.repository.UserRepository userRepository;
//
//    public NotificationController(NotificationService notificationService, com.example.rideshare.user.repository.UserRepository userRepository) {
//        this.notificationService = notificationService;
//        this.userRepository = userRepository;
//    }
//
//    // GET /api/notifications
//    // Uses the logged-in user's email from the JWT token
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications() {
//        // Extract email from Security Context (set by JwtAuthFilter)
//        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//
//        // Find User ID
//        Long userId = userRepository.findByEmail(email).orElseThrow().getId();
//
//        List<NotificationDto> notifications = notificationService.getUserNotifications(userId);
//        return ResponseEntity.ok(ApiResponse.success("Notifications retrieved", notifications));
//    }
//}

package com.example.rideshare.notification.controller;

import com.example.rideshare.common.ApiResponse;
import com.example.rideshare.notification.dto.NotificationDto;
import com.example.rideshare.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "User inbox for system alerts and messages")
public class NotificationController {

    private final NotificationService notificationService;
    private final com.example.rideshare.user.repository.UserRepository userRepository;

    public NotificationController(NotificationService notificationService, com.example.rideshare.user.repository.UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Get My Notifications", description = "Fetches the inbox for the currently logged-in user (extracted from JWT).")
    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getMyNotifications() {
        // Extract email from Security Context (set by JwtAuthFilter)
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Find User ID
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        List<NotificationDto> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(ApiResponse.success("Notifications retrieved", notifications));
    }
}