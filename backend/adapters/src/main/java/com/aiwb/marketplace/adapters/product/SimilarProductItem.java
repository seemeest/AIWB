package com.aiwb.marketplace.adapters.product;

import java.math.BigDecimal;
import java.util.UUID;

public record SimilarProductItem(UUID id,
                                 UUID sellerId,
                                 String title,
                                 String description,
                                 BigDecimal price,
                                 String status) {
}
