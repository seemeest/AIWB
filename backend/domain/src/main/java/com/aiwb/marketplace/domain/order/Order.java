package com.aiwb.marketplace.domain.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Order {
    private final UUID id;
    private final UUID buyerId;
    private final List<OrderItem> items;
    private final OrderStatus status;
    private final BigDecimal totalAmount;
    private final Instant createdAt;
    private final Instant updatedAt;

    private Order(UUID id,
                  UUID buyerId,
                  List<OrderItem> items,
                  OrderStatus status,
                  BigDecimal totalAmount,
                  Instant createdAt,
                  Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.buyerId = Objects.requireNonNull(buyerId, "buyerId");
        this.items = List.copyOf(Objects.requireNonNull(items, "items"));
        this.status = Objects.requireNonNull(status, "status");
        this.totalAmount = Objects.requireNonNull(totalAmount, "totalAmount");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
    }

    public static Order create(UUID id, UUID buyerId, List<OrderItem> items, Instant now) {
        BigDecimal total = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Order(id, buyerId, items, OrderStatus.CREATED, total, now, now);
    }

    public static Order restore(UUID id,
                                UUID buyerId,
                                List<OrderItem> items,
                                OrderStatus status,
                                BigDecimal totalAmount,
                                Instant createdAt,
                                Instant updatedAt) {
        return new Order(id, buyerId, items, status, totalAmount, createdAt, updatedAt);
    }

    public Order markPaid(Instant now) {
        return new Order(id, buyerId, items, OrderStatus.PAID, totalAmount, createdAt, now);
    }

    public Order updateStatus(OrderStatus status, Instant now) {
        return new Order(id, buyerId, items, status, totalAmount, createdAt, now);
    }

    public UUID getId() {
        return id;
    }

    public UUID getBuyerId() {
        return buyerId;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
