package com.aiwb.marketplace.adapters.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotNull UUID buyerId,
        @NotNull List<Item> items
) {
    public record Item(
            @NotNull UUID productId,
            @NotNull UUID sellerId,
            @Positive int quantity,
            @NotNull BigDecimal price
    ) {
    }
}
