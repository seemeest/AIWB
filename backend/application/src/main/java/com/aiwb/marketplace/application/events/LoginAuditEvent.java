package com.aiwb.marketplace.application.events;

import java.time.Instant;
import java.util.UUID;

public record LoginAuditEvent(UUID userId,
                              String ip,
                              String userAgent,
                              Instant occurredAt) {
}
