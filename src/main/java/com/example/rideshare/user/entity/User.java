package com.example.rideshare.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") // 'user' is a reserved keyword in Postgres
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // Will be hashed later

    @Column(nullable = false)
    private String fullName;

    private String phone;

    // Start with simple string roles: "PASSENGER", "DRIVER", "ADMIN"
    private String roles = "PASSENGER";

    private String profilePictureUrl;
}