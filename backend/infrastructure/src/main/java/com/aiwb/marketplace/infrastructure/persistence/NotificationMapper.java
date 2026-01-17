package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.domain.notification.Notification;

public final class NotificationMapper {
    private NotificationMapper() {
    }

    public static Notification toDomain(NotificationEntity entity) {
        if (entity == null) {
            return null;
        }
        return Notification.restore(
                entity.getId(),
                entity.getUserId(),
                entity.getChannel(),
                entity.getType(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getLink(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getSentAt(),
                entity.getReadAt()
        );
    }

    public static NotificationEntity toEntity(Notification notification) {
        NotificationEntity entity = new NotificationEntity();
        entity.setId(notification.getId());
        entity.setUserId(notification.getUserId());
        entity.setChannel(notification.getChannel());
        entity.setType(notification.getType());
        entity.setTitle(notification.getTitle());
        entity.setMessage(notification.getMessage());
        entity.setLink(notification.getLink());
        entity.setStatus(notification.getStatus());
        entity.setCreatedAt(notification.getCreatedAt());
        entity.setSentAt(notification.getSentAt());
        entity.setReadAt(notification.getReadAt());
        return entity;
    }
}
