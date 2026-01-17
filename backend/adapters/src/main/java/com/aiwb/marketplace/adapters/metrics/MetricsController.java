package com.aiwb.marketplace.adapters.metrics;

import com.aiwb.marketplace.application.metrics.MetricsService;
import com.aiwb.marketplace.application.metrics.SellerMetrics;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {
    private final MetricsService metricsService;
    private final Clock clock;

    public MetricsController(MetricsService metricsService, Clock clock) {
        this.metricsService = metricsService;
        this.clock = clock;
    }

    @GetMapping("/sellers/{sellerId}")
    public ResponseEntity<SellerMetricsResponse> getSellerMetrics(@PathVariable("sellerId") UUID sellerId,
                                                                  @RequestParam(value = "from", required = false)
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                  Instant from,
                                                                  @RequestParam(value = "to", required = false)
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                  Instant to) {
        Instant resolvedTo = to == null ? clock.instant() : to;
        Instant resolvedFrom = from == null ? resolvedTo.minus(30, ChronoUnit.DAYS) : from;
        SellerMetrics metrics = metricsService.getSellerMetrics(sellerId, resolvedFrom, resolvedTo);
        return ResponseEntity.ok(new SellerMetricsResponse(
                metrics.sellerId(),
                metrics.from(),
                metrics.to(),
                metrics.uniqueViews(),
                metrics.averageSearchPosition(),
                metrics.orderCount(),
                metrics.conversion()
        ));
    }
}
