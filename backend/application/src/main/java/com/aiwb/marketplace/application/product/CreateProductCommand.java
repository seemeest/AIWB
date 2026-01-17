package com.aiwb.marketplace.application.product;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductCommand(UUID sellerId, String title, String description, BigDecimal price, int quantity) {
}
