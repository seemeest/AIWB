package com.aiwb.marketplace.application.order;

import com.aiwb.marketplace.application.ports.DeliveryRepository;
import com.aiwb.marketplace.application.ports.OrderRepository;
import com.aiwb.marketplace.application.ports.PaymentService;
import com.aiwb.marketplace.application.notification.NotificationService;
import com.aiwb.marketplace.domain.notification.NotificationType;
import com.aiwb.marketplace.domain.order.DeliveryStatus;
import com.aiwb.marketplace.domain.order.Order;
import com.aiwb.marketplace.domain.order.OrderItem;
import com.aiwb.marketplace.domain.order.OrderStatus;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderService {
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;
    private final DeliveryRepository deliveryRepository;
    private final Clock clock;
    private final NotificationService notificationService;

    public OrderService(OrderRepository orderRepository,
                        PaymentService paymentService,
                        DeliveryRepository deliveryRepository,
                        Clock clock,
                        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.deliveryRepository = deliveryRepository;
        this.clock = clock;
        this.notificationService = notificationService;
    }

    public Order create(CreateOrderCommand command) {
        Instant now = clock.instant();
        List<OrderItem> items = command.items().stream()
                .map(item -> new OrderItem(item.productId(), item.sellerId(), item.quantity(), item.price()))
                .toList();
        Order order = Order.create(UUID.randomUUID(), command.buyerId(), items, now);
        Order saved = orderRepository.save(order);
        deliveryRepository.createShipment(saved.getId(), DeliveryStatus.CREATED, now);
        paymentService.createPayment(saved.getId(), saved.getTotalAmount());
        notificationService.notifyUser(saved.getBuyerId(), NotificationType.ORDER_CREATED,
                "Заказ создан",
                "Заказ " + saved.getId() + " создан.",
                "/orders/" + saved.getId());
        return saved;
    }

    public Order markPaid(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        Order updated = order.markPaid(clock.instant());
        Order saved = orderRepository.save(updated);
        deliveryRepository.addStatus(orderId, DeliveryStatus.PAID, clock.instant());
        notificationService.notifyUser(saved.getBuyerId(), NotificationType.ORDER_PAID,
                "Заказ оплачен",
                "Заказ " + saved.getId() + " оплачен.",
                "/orders/" + saved.getId());
        return saved;
    }

    public Order updateDeliveryStatus(UpdateDeliveryStatusCommand command) {
        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        OrderStatus status = switch (command.status()) {
            case CREATED -> OrderStatus.CREATED;
            case PAID -> OrderStatus.PAID;
            case ACCEPTED -> OrderStatus.ACCEPTED;
            case ASSEMBLED -> OrderStatus.ASSEMBLED;
            case SHIPPED -> OrderStatus.SHIPPED;
            case IN_TRANSIT -> OrderStatus.IN_TRANSIT;
            case ARRIVED_AT_PVZ -> OrderStatus.ARRIVED_AT_PVZ;
            case READY_FOR_PICKUP -> OrderStatus.READY_FOR_PICKUP;
            case PICKED_UP -> OrderStatus.PICKED_UP;
            case COMPLETED -> OrderStatus.COMPLETED;
            case CANCELLED -> OrderStatus.CANCELLED;
            case RETURNED -> OrderStatus.RETURNED;
            case LOST -> OrderStatus.LOST;
            case EXPIRED -> OrderStatus.EXPIRED;
        };
        Order updated = order.updateStatus(status, clock.instant());
        Order saved = orderRepository.save(updated);
        deliveryRepository.addStatus(order.getId(), command.status(), clock.instant());
        notificationService.notifyUser(saved.getBuyerId(), NotificationType.DELIVERY_STATUS_CHANGED,
                "Статус доставки изменен",
                "Заказ " + saved.getId() + " теперь в статусе " + saved.getStatus().name() + ".",
                "/orders/" + saved.getId());
        return saved;
    }
}
