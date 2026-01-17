package com.aiwb.marketplace.adapters.notification;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(UUID id,
                                   UUID userId,
                                   String channel,
                                   String type,
                                   String title,
                                   String message,
                                   String link,
                                   String status,
                                   Instant createdAt,
                                   Instant sentAt,
                                   Instant readAt) {
}
