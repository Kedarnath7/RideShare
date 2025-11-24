package com.example.rideshare.notification.repository;

import com.example.rideshare.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Find all notifications for a specific user, ordered by newest first
    List<Notification> findByRecipientIdOrderByTimestampDesc(Long userId);
}