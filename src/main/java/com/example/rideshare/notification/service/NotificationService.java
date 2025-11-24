package com.example.rideshare.notification.service;

import com.example.rideshare.notification.dto.NotificationDto;
import com.example.rideshare.notification.entity.Notification;
import com.example.rideshare.notification.repository.NotificationRepository;
import com.example.rideshare.user.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // 1. Send Notification (Internal Use)
    public void sendNotification(User recipient, String message) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);

        notificationRepository.save(notification);
        System.out.println("🔔 Notification sent to " + recipient.getEmail() + ": " + message);
    }

    // 2. Get Notifications for User
    public List<NotificationDto> getUserNotifications(Long userId) {
        return notificationRepository.findByRecipientIdOrderByTimestampDesc(userId)
                .stream()
                .map(n -> new NotificationDto(n.getId(), n.getMessage(), n.getTimestamp(), n.isRead()))
                .collect(Collectors.toList());
    }
}