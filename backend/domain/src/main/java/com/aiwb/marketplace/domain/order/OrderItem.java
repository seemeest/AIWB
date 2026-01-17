package com.aiwb.marketplace.domain.order;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public final class OrderItem {
    private final UUID productId;
    private final UUID sellerId;
    private final int quantity;
    private final BigDecimal price;

    public OrderItem(UUID productId, UUID sellerId, int quantity, BigDecimal price) {
        this.productId = Objects.requireNonNull(productId, "productId");
        this.sellerId = Objects.requireNonNull(sellerId, "sellerId");
        this.quantity = quantity;
        this.price = Objects.requireNonNull(price, "price");
    }

    public UUID getProductId() {
        return productId;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
