package com.aiwb.marketplace.application.product;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductCommand(UUID productId,
                                   UUID sellerId,
                                   String title,
                                   String description,
                                   BigDecimal price,
                                   Integer quantity) {
}
