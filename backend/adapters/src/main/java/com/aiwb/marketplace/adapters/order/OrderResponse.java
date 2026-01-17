package com.aiwb.marketplace.adapters.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID buyerId,
        String status,
        BigDecimal totalAmount,
        List<Item> items
) {
    public record Item(UUID productId, UUID sellerId, int quantity, BigDecimal price) {
    }
}
