package com.example.rideshare.user.service;

import com.example.rideshare.user.dto.UserDto;
import com.example.rideshare.user.entity.User;
import com.example.rideshare.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Get User by ID
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToDto(user);
    }

    // 2. Create User (Used for testing registration for now)
    public UserDto createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    // Helper: Map Entity to DTO
    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRoles(),
                user.getProfilePictureUrl()
        );
    }
}