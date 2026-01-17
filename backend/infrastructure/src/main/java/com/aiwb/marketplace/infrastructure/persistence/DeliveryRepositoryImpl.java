package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.ports.DeliveryRepository;
import com.aiwb.marketplace.domain.order.DeliveryStatus;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public class DeliveryRepositoryImpl implements DeliveryRepository {
    private final JpaShipmentRepository shipmentRepository;
    private final JpaShipmentStatusRepository statusRepository;

    public DeliveryRepositoryImpl(JpaShipmentRepository shipmentRepository,
                                  JpaShipmentStatusRepository statusRepository) {
        this.shipmentRepository = shipmentRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public void createShipment(UUID orderId, DeliveryStatus status, Instant createdAt) {
        ShipmentEntity shipment = new ShipmentEntity();
        shipment.setId(UUID.randomUUID());
        shipment.setOrderId(orderId);
        shipment.setCurrentStatus(status);
        shipment.setCreatedAt(createdAt);
        shipmentRepository.save(shipment);
        addStatus(orderId, status, createdAt);
    }

    @Override
    public void addStatus(UUID orderId, DeliveryStatus status, Instant createdAt) {
        ShipmentStatusEntity history = new ShipmentStatusEntity();
        history.setId(UUID.randomUUID());
        history.setOrderId(orderId);
        history.setStatus(status);
        history.setCreatedAt(createdAt);
        statusRepository.save(history);
        shipmentRepository.findByOrderId(orderId).ifPresent(shipment -> {
            shipment.setCurrentStatus(status);
            shipmentRepository.save(shipment);
        });
    }
}
