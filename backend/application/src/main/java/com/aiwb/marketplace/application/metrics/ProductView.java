package com.aiwb.marketplace.application.metrics;

import java.time.Instant;
import java.util.UUID;

public record ProductView(UUID id,
                          UUID productId,
                          UUID sellerId,
                          UUID viewerId,
                          String sessionId,
                          Instant viewedAt) {
}
