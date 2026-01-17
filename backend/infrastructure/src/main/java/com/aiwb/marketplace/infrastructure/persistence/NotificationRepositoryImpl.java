package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.ports.NotificationRepository;
import com.aiwb.marketplace.domain.notification.Notification;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {
    private final JpaNotificationRepository repository;

    public NotificationRepositoryImpl(JpaNotificationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = NotificationMapper.toEntity(notification);
        return NotificationMapper.toDomain(repository.save(entity));
    }

    @Override
    public List<Notification> findByUserId(UUID userId, int limit) {
        List<NotificationEntity> entities =
                repository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(0, Math.max(1, limit)));
        return entities.stream().map(NotificationMapper::toDomain).toList();
    }

    @Override
    public Notification markRead(UUID notificationId, Instant readAt) {
        NotificationEntity entity = repository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        entity.setReadAt(readAt);
        entity.setStatus(com.aiwb.marketplace.domain.notification.NotificationStatus.READ);
        return NotificationMapper.toDomain(repository.save(entity));
    }
}
