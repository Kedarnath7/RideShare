package com.example.rideshare.payment.strategy;

import com.example.rideshare.booking.entity.Booking;
import com.example.rideshare.payment.entity.Payment;
import com.example.rideshare.payment.entity.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class CashStrategy implements PaymentStrategy {

    @Override
    public boolean supports(String paymentMethod) {
        return "CASH".equalsIgnoreCase(paymentMethod);
    }

    @Override
    public Payment process(Booking booking, Double amount) {
        // Cash payments don't have a digital transaction ID, so we generate an internal ref
        String internalRef = "cash_" + UUID.randomUUID().toString().substring(0, 8);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentMethod("CASH");

        // Cash is usually marked COMPLETED only when driver confirms.
        // For this MVP, we assume the driver clicked "Collect Cash" on their app.
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionId(internalRef);

        return payment;
    }
}