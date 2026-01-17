package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.notification.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository {
    Notification save(Notification notification);

    List<Notification> findByUserId(UUID userId, int limit);

    Notification markRead(UUID notificationId, java.time.Instant readAt);
}
