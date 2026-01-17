package com.aiwb.marketplace.adapters.order;

import com.aiwb.marketplace.domain.order.DeliveryStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateDeliveryStatusRequest(@NotNull DeliveryStatus status) {
}
