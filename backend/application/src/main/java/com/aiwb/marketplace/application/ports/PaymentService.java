package com.aiwb.marketplace.application.ports;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    String createPayment(UUID orderId, BigDecimal amount);
}
