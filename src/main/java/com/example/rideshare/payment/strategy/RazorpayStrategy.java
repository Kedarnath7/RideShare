package com.example.rideshare.payment.strategy;

import com.example.rideshare.booking.entity.Booking;
import com.example.rideshare.payment.entity.Payment;
import com.example.rideshare.payment.entity.PaymentStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RazorpayStrategy implements PaymentStrategy {

    @Override
    public boolean supports(String paymentMethod) {
        return "RAZORPAY".equalsIgnoreCase(paymentMethod) || "UPI".equalsIgnoreCase(paymentMethod);
    }

    @Override
    public Payment process(Booking booking, Double amount) {
        // --- REAL WORLD LOGIC STUB ---
        // RazorpayClient client = new RazorpayClient("key", "secret");
        // Order order = client.orders.create(jsonParams);
        // String transactionId = order.get("id");
        // -----------------------------

        // SIMULATION: Generate a fake Razorpay Order ID
        String fakeTransactionId = "pay_" + UUID.randomUUID().toString().substring(0, 10);

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentMethod("RAZORPAY");
        payment.setStatus(PaymentStatus.COMPLETED); // Assume success for MVP
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionId(fakeTransactionId);

        return payment;
    }
}