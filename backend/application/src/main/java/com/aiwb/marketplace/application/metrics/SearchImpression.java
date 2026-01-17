package com.aiwb.marketplace.application.metrics;

import java.time.Instant;
import java.util.UUID;

public record SearchImpression(UUID id,
                               UUID productId,
                               UUID sellerId,
                               String query,
                               int position,
                               Instant shownAt) {
}
