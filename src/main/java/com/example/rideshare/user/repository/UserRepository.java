package com.example.rideshare.user.repository;

import com.example.rideshare.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA automatically generates the SQL for these:

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}