package com.example.rideshare.auth.controller;

import com.example.rideshare.auth.dto.AuthRequest;
import com.example.rideshare.auth.dto.AuthResponse;
import com.example.rideshare.auth.service.AuthService;
import com.example.rideshare.common.ApiResponse;
import com.example.rideshare.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody User user) {
        return ResponseEntity.ok(ApiResponse.success("User registered", authService.register(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Login successful", authService.login(request)));
    }
}