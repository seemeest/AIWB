package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.domain.order.Order;
import com.aiwb.marketplace.domain.order.OrderItem;

import java.util.List;
import java.util.UUID;

public final class OrderMapper {
    private OrderMapper() {
    }

    public static Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }
        List<OrderItem> items = entity.getItems().stream()
                .map(item -> new OrderItem(
                        item.getProductId(),
                        item.getSellerId(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();
        return Order.restore(
                entity.getId(),
                entity.getBuyerId(),
                items,
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setBuyerId(order.getBuyerId());
        entity.setStatus(order.getStatus());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());

        List<OrderItemEntity> items = order.getItems().stream()
                .map(item -> {
                    OrderItemEntity itemEntity = new OrderItemEntity();
                    itemEntity.setId(UUID.randomUUID());
                    itemEntity.setOrder(entity);
                    itemEntity.setProductId(item.getProductId());
                    itemEntity.setSellerId(item.getSellerId());
                    itemEntity.setQuantity(item.getQuantity());
                    itemEntity.setPrice(item.getPrice());
                    return itemEntity;
                })
                .toList();
        entity.setItems(items);
        return entity;
    }
}
