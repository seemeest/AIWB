package com.aiwb.marketplace.application.search;

import java.math.BigDecimal;
import java.util.UUID;

public record SearchResult(UUID id, String title, String description, BigDecimal price, String status) {
}
