package com.example.rideshare.auth.service;

import com.example.rideshare.auth.dto.AuthRequest;
import com.example.rideshare.auth.dto.AuthResponse;
import com.example.rideshare.auth.util.JwtUtil;
import com.example.rideshare.user.entity.User;
import com.example.rideshare.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    // 1. Register User (With Hashed Password)
    public AuthResponse register(User user) {
        // Hash the password before saving!
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getEmail());

        return new AuthResponse(token, savedUser.getId(), savedUser.getFullName(), savedUser.getRoles());
    }

    // 2. Login User
    public AuthResponse login(AuthRequest request) {
        // Authenticate using Spring Security Manager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        if (authentication.isAuthenticated()) {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            String token = jwtUtil.generateToken(request.getEmail());
            return new AuthResponse(token, user.getId(), user.getFullName(), user.getRoles());
        } else {
            throw new RuntimeException("Invalid Access");
        }
    }
}