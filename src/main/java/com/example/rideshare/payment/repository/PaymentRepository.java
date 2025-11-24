package com.example.rideshare.payment.repository;

import com.example.rideshare.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by the unique transaction reference
    Optional<Payment> findByTransactionId(String transactionId);

    // Find payment associated with a booking
    Optional<Payment> findByBookingId(Long bookingId);
}