package com.aiwb.marketplace.adapters.product;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductRequest(@NotNull UUID sellerId,
                                   String title,
                                   String description,
                                   BigDecimal price,
                                   Integer quantity) {
}
