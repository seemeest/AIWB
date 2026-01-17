package com.aiwb.marketplace.adapters.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(
        @NotNull UUID sellerId,
        @NotBlank String title,
        String description,
        @NotNull BigDecimal price,
        @PositiveOrZero int quantity
) {
}
