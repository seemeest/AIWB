package com.aiwb.marketplace.application.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(UUID buyerId, List<Item> items) {

    public record Item(UUID productId, UUID sellerId, int quantity, BigDecimal price) {
    }
}
