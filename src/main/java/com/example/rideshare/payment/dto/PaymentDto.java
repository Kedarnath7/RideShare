package com.example.rideshare.payment.dto;

import com.example.rideshare.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long id;
    private String transactionId;
    private Double amount;
    private String paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paymentTime;

    // Context
    private Long bookingId;
}