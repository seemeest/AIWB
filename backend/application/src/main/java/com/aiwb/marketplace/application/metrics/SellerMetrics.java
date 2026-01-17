package com.aiwb.marketplace.application.metrics;

import java.time.Instant;
import java.util.UUID;

public record SellerMetrics(UUID sellerId,
                            Instant from,
                            Instant to,
                            long uniqueViews,
                            double averageSearchPosition,
                            long orderCount,
                            double conversion) {
}
