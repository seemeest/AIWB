package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.application.metrics.ProductView;
import com.aiwb.marketplace.application.metrics.SearchImpression;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MetricsRepository {
    void saveProductView(ProductView view);

    void saveSearchImpressions(List<SearchImpression> impressions);

    long countUniqueViews(UUID sellerId, Instant from, Instant to);

    double averageSearchPosition(UUID sellerId, Instant from, Instant to);

    long countOrders(UUID sellerId, Instant from, Instant to);
}
