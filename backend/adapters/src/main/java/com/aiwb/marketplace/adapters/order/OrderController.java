package com.aiwb.marketplace.adapters.order;

import com.aiwb.marketplace.application.order.CreateOrderCommand;
import com.aiwb.marketplace.application.order.OrderService;
import com.aiwb.marketplace.application.order.UpdateDeliveryStatusCommand;
import com.aiwb.marketplace.domain.order.Order;
import com.aiwb.marketplace.domain.order.OrderItem;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.create(new CreateOrderCommand(
                request.buyerId(),
                request.items().stream()
                        .map(item -> new CreateOrderCommand.Item(
                                item.productId(),
                                item.sellerId(),
                                item.quantity(),
                                item.price()
                        ))
                        .toList()
        ));
        return ResponseEntity.ok(toResponse(order));
    }

    @PatchMapping("/{id}/payment")
    public ResponseEntity<OrderResponse> markPaid(@PathVariable("id") UUID orderId) {
        Order order = orderService.markPaid(orderId);
        return ResponseEntity.ok(toResponse(order));
    }

    @PatchMapping("/{id}/delivery-status")
    public ResponseEntity<OrderResponse> updateDelivery(@PathVariable("id") UUID orderId,
                                                        @Valid @RequestBody UpdateDeliveryStatusRequest request) {
        Order order = orderService.updateDeliveryStatus(new UpdateDeliveryStatusCommand(orderId, request.status()));
        return ResponseEntity.ok(toResponse(order));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderResponse.Item> items = order.getItems().stream()
                .map(item -> new OrderResponse.Item(
                        item.getProductId(),
                        item.getSellerId(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();
        return new OrderResponse(order.getId(), order.getBuyerId(), order.getStatus().name(), order.getTotalAmount(), items);
    }
}
