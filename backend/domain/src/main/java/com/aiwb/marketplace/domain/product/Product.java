package com.aiwb.marketplace.domain.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Product {
    private final UUID id;
    private final UUID sellerId;
    private final String title;
    private final String description;
    private final BigDecimal price;
    private final int quantity;
    private final ProductStatus status;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final List<ProductImage> images;

    private Product(UUID id,
                    UUID sellerId,
                    String title,
                    String description,
                    BigDecimal price,
                    int quantity,
                    ProductStatus status,
                    Instant createdAt,
                    Instant updatedAt,
                    List<ProductImage> images) {
        this.id = Objects.requireNonNull(id, "id");
        this.sellerId = Objects.requireNonNull(sellerId, "sellerId");
        this.title = Objects.requireNonNull(title, "title");
        this.description = description;
        this.price = Objects.requireNonNull(price, "price");
        this.quantity = quantity;
        this.status = Objects.requireNonNull(status, "status");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt");
        this.images = new ArrayList<>(Objects.requireNonNull(images, "images"));
    }

    public static Product draft(UUID id,
                                UUID sellerId,
                                String title,
                                String description,
                                BigDecimal price,
                                int quantity,
                                Instant now) {
        return new Product(id, sellerId, title, description, price, quantity,
                ProductStatus.DRAFT, now, now, List.of());
    }

    public static Product restore(UUID id,
                                  UUID sellerId,
                                  String title,
                                  String description,
                                  BigDecimal price,
                                  int quantity,
                                  ProductStatus status,
                                  Instant createdAt,
                                  Instant updatedAt,
                                  List<ProductImage> images) {
        return new Product(id, sellerId, title, description, price, quantity,
                status, createdAt, updatedAt, images);
    }

    public Product addImage(ProductImage image) {
        List<ProductImage> nextImages = new ArrayList<>(images);
        nextImages.add(image);
        return new Product(id, sellerId, title, description, price, quantity, status, createdAt, Instant.now(), nextImages);
    }

    public Product publish() {
        if (status == ProductStatus.PUBLISHED) {
            return this;
        }
        return new Product(id, sellerId, title, description, price, quantity,
                ProductStatus.PUBLISHED, createdAt, Instant.now(), images);
    }

    public UUID getId() {
        return id;
    }

    public UUID getSellerId() {
        return sellerId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public List<ProductImage> getImages() {
        return Collections.unmodifiableList(images);
    }
}
