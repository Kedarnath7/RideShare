package com.example.rideshare.payment.strategy;

import com.example.rideshare.booking.entity.Booking;
import com.example.rideshare.payment.entity.Payment;

public interface PaymentStrategy {
    // Each strategy must verify the "method name" it handles (e.g., "UPI", "CASH")
    boolean supports(String paymentMethod);

    // The core logic: How do we process this specific type of payment?
    Payment process(Booking booking, Double amount);
}