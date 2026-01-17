package com.aiwb.marketplace.adapters.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID sellerId,
        String title,
        String description,
        BigDecimal price,
        int quantity,
        String status,
        List<String> images
) {
}
