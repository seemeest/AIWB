package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.order.DeliveryStatus;

import java.time.Instant;
import java.util.UUID;

public interface DeliveryRepository {
    void createShipment(UUID orderId, DeliveryStatus status, Instant createdAt);
    void addStatus(UUID orderId, DeliveryStatus status, Instant createdAt);
}
