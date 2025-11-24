package com.example.rideshare.payment.service;

import com.example.rideshare.booking.entity.Booking;
import com.example.rideshare.booking.repository.BookingRepository;
import com.example.rideshare.notification.service.NotificationService;
import com.example.rideshare.payment.dto.PaymentDto;
import com.example.rideshare.payment.entity.Payment;
import com.example.rideshare.payment.entity.PaymentStatus;
import com.example.rideshare.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          NotificationService notificationService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public PaymentDto processPayment(Long bookingId, String paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (paymentRepository.findByBookingId(bookingId).isPresent()) {
            throw new RuntimeException("Booking is already paid for.");
        }

        Double totalAmount = booking.getRide().getPricePerSeat() * booking.getSeatsBooked();

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        Payment savedPayment = paymentRepository.save(payment);

        // NOTIFICATION: To Passenger
        notificationService.sendNotification(
                booking.getPassenger(),
                "Payment Successful! Paid $" + totalAmount + " for your ride to " + booking.getRide().getDestination()
        );

        return mapToDto(savedPayment);
    }

    public PaymentDto getPaymentByBooking(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("No payment found for this booking"));
        return mapToDto(payment);
    }

    private PaymentDto mapToDto(Payment payment) {
        return new PaymentDto(
                payment.getId(),
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getPaymentTime(),
                payment.getBooking().getId()
        );
    }
}