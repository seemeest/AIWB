package com.aiwb.marketplace.adapters.metrics;

import java.time.Instant;
import java.util.UUID;

public record SellerMetricsResponse(UUID sellerId,
                                    Instant from,
                                    Instant to,
                                    long uniqueViews,
                                    double averageSearchPosition,
                                    long orderCount,
                                    double conversion) {
}
