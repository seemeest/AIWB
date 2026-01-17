package com.aiwb.marketplace.application.metrics;

import com.aiwb.marketplace.application.ports.MetricsRepository;
import com.aiwb.marketplace.application.search.SearchResult;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MetricsService {
    private final MetricsRepository metricsRepository;
    private final Clock clock;

    public MetricsService(MetricsRepository metricsRepository, Clock clock) {
        this.metricsRepository = metricsRepository;
        this.clock = clock;
    }

    public void recordProductView(UUID productId, UUID sellerId, UUID viewerId, String sessionId) {
        if (viewerId == null && (sessionId == null || sessionId.isBlank())) {
            return;
        }
        ProductView view = new ProductView(
                UUID.randomUUID(),
                productId,
                sellerId,
                viewerId,
                sessionId,
                clock.instant()
        );
        metricsRepository.saveProductView(view);
    }

    public void recordSearchImpressions(String query, List<SearchResult> results) {
        if (results == null || results.isEmpty()) {
            return;
        }
        Instant now = clock.instant();
        List<SearchImpression> impressions = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            if (result.sellerId() == null) {
                continue;
            }
            impressions.add(new SearchImpression(
                    UUID.randomUUID(),
                    result.id(),
                    result.sellerId(),
                    query,
                    i + 1,
                    now
            ));
        }
        metricsRepository.saveSearchImpressions(impressions);
    }

    public SellerMetrics getSellerMetrics(UUID sellerId, Instant from, Instant to) {
        long uniqueViews = metricsRepository.countUniqueViews(sellerId, from, to);
        double avgPosition = metricsRepository.averageSearchPosition(sellerId, from, to);
        long orders = metricsRepository.countOrders(sellerId, from, to);
        double conversion = uniqueViews == 0 ? 0.0 : (double) orders / uniqueViews;
        return new SellerMetrics(sellerId, from, to, uniqueViews, avgPosition, orders, conversion);
    }
}
