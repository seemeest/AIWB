package com.aiwb.marketplace.domain.notification;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Notification {
    private final UUID id;
    private final UUID userId;
    private final NotificationChannel channel;
    private final NotificationType type;
    private final String title;
    private final String message;
    private final String link;
    private final NotificationStatus status;
    private final Instant createdAt;
    private final Instant sentAt;
    private final Instant readAt;

    private Notification(UUID id,
                         UUID userId,
                         NotificationChannel channel,
                         NotificationType type,
                         String title,
                         String message,
                         String link,
                         NotificationStatus status,
                         Instant createdAt,
                         Instant sentAt,
                         Instant readAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.userId = Objects.requireNonNull(userId, "userId");
        this.channel = Objects.requireNonNull(channel, "channel");
        this.type = Objects.requireNonNull(type, "type");
        this.title = Objects.requireNonNull(title, "title");
        this.message = Objects.requireNonNull(message, "message");
        this.link = link;
        this.status = Objects.requireNonNull(status, "status");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.sentAt = sentAt;
        this.readAt = readAt;
    }

    public static Notification create(UUID id,
                                      UUID userId,
                                      NotificationChannel channel,
                                      NotificationType type,
                                      String title,
                                      String message,
                                      String link,
                                      Instant createdAt) {
        return new Notification(id, userId, channel, type, title, message, link,
                NotificationStatus.PENDING, createdAt, null, null);
    }

    public static Notification restore(UUID id,
                                       UUID userId,
                                       NotificationChannel channel,
                                       NotificationType type,
                                       String title,
                                       String message,
                                       String link,
                                       NotificationStatus status,
                                       Instant createdAt,
                                       Instant sentAt,
                                       Instant readAt) {
        return new Notification(id, userId, channel, type, title, message, link, status, createdAt, sentAt, readAt);
    }

    public Notification markSent(Instant sentAt) {
        return new Notification(id, userId, channel, type, title, message, link,
                NotificationStatus.SENT, createdAt, sentAt, readAt);
    }

    public Notification markFailed(Instant failedAt) {
        return new Notification(id, userId, channel, type, title, message, link,
                NotificationStatus.FAILED, createdAt, failedAt, readAt);
    }

    public Notification markRead(Instant readAt) {
        return new Notification(id, userId, channel, type, title, message, link,
                NotificationStatus.READ, createdAt, sentAt, readAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public NotificationType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getLink() {
        return link;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public Instant getReadAt() {
        return readAt;
    }
}
