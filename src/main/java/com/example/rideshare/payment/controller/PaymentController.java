package com.example.rideshare.payment.controller;

import com.example.rideshare.common.ApiResponse;
import com.example.rideshare.payment.dto.PaymentDto;
import com.example.rideshare.payment.service.PaymentService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // POST /api/payments
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentDto>> makePayment(@RequestBody PaymentRequest request) {
        try {
            PaymentDto payment = paymentService.processPayment(
                    request.getBookingId(),
                    request.getPaymentMethod()
            );
            return ResponseEntity.ok(ApiResponse.success("Payment successful", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    // GET /api/payments/{bookingId}
    @GetMapping("/{bookingId}")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentDetails(@PathVariable Long bookingId) {
        try {
            PaymentDto payment = paymentService.getPaymentByBooking(bookingId);
            return ResponseEntity.ok(ApiResponse.success("Payment details found", payment));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @Data
    public static class PaymentRequest {
        private Long bookingId;
        private String paymentMethod;
    }
}