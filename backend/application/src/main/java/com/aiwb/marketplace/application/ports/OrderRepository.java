package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.order.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(UUID id);
}
