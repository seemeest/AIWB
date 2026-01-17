package com.aiwb.marketplace.domain.product;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class ProductImage {
    private final UUID id;
    private final String path;
    private final int sortOrder;
    private final Instant createdAt;

    public ProductImage(UUID id, String path, int sortOrder, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.path = Objects.requireNonNull(path, "path");
        this.sortOrder = sortOrder;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public UUID getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
