package com.aiwb.marketplace.infrastructure.payment;

import com.aiwb.marketplace.application.ports.PaymentService;
import com.aiwb.marketplace.domain.order.PaymentStatus;
import com.aiwb.marketplace.infrastructure.persistence.JpaPaymentRepository;
import com.aiwb.marketplace.infrastructure.persistence.PaymentEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Component
public class YookassaPaymentService implements PaymentService {
    private final JpaPaymentRepository paymentRepository;
    private final Clock clock;

    public YookassaPaymentService(JpaPaymentRepository paymentRepository, Clock clock) {
        this.paymentRepository = paymentRepository;
        this.clock = clock;
    }

    @Override
    public String createPayment(UUID orderId, BigDecimal amount) {
        Instant now = clock.instant();
        String externalId = "yk-" + UUID.randomUUID();
        PaymentEntity entity = new PaymentEntity();
        entity.setId(UUID.randomUUID());
        entity.setOrderId(orderId);
        entity.setProvider("YOOKASSA");
        entity.setExternalId(externalId);
        entity.setStatus(PaymentStatus.PENDING);
        entity.setAmount(amount);
        entity.setCreatedAt(now);
        paymentRepository.save(entity);
        return externalId;
    }
}
