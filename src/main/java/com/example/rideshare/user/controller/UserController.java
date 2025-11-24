//package com.example.rideshare.user.controller;
//
//import com.example.rideshare.common.ApiResponse;
//import com.example.rideshare.user.dto.UserDto;
//import com.example.rideshare.user.entity.User;
//import com.example.rideshare.user.service.UserService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    // GET /api/users/{id}
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<UserDto>> getUserProfile(@PathVariable Long id) {
//        try {
//            UserDto user = userService.getUserById(id);
//            return ResponseEntity.ok(ApiResponse.success("User found", user));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(ApiResponse.error(e.getMessage()));
//        }
//    }
//
//    // POST /api/users (Temporary, just to add data to DB)
//    @PostMapping
//    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody User user) {
//        try {
//            UserDto createdUser = userService.createUser(user);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(ApiResponse.success("User created successfully", createdUser));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(ApiResponse.error(e.getMessage()));
//        }
//    }
//}

package com.example.rideshare.user.controller;

import com.example.rideshare.common.ApiResponse;
import com.example.rideshare.user.dto.UserDto;
import com.example.rideshare.user.entity.User;
import com.example.rideshare.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Profiles", description = "Manage user profiles and details")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get User Profile", description = "Fetch public profile details for a user by ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserProfile(@PathVariable Long id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(ApiResponse.success("User found", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Operation(summary = "Create User (Dev Only)", description = "Directly creates a user without hashing passwords. Use /api/auth/register for real users.")
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody User user) {
        try {
            UserDto createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("User created successfully", createdUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}