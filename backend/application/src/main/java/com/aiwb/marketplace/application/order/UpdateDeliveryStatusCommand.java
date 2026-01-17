package com.aiwb.marketplace.application.order;

import com.aiwb.marketplace.domain.order.DeliveryStatus;

import java.util.UUID;

public record UpdateDeliveryStatusCommand(UUID orderId, DeliveryStatus status) {
}
