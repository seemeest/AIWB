package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.domain.product.Product;
import com.aiwb.marketplace.domain.product.ProductImage;

import java.util.List;
import java.util.stream.Collectors;

public final class ProductMapper {
    private ProductMapper() {
    }

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        List<ProductImage> images = entity.getImages().stream()
                .map(image -> new ProductImage(
                        image.getId(),
                        image.getPath(),
                        image.getSortOrder(),
                        image.getCreatedAt()
                ))
                .collect(Collectors.toList());
        return Product.restore(
                entity.getId(),
                entity.getSellerId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getQuantity(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                images
        );
    }

    public static ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setSellerId(product.getSellerId());
        entity.setTitle(product.getTitle());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setQuantity(product.getQuantity());
        entity.setStatus(product.getStatus());
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());

        List<ProductImageEntity> images = product.getImages().stream()
                .map(image -> {
                    ProductImageEntity imageEntity = new ProductImageEntity();
                    imageEntity.setId(image.getId());
                    imageEntity.setProduct(entity);
                    imageEntity.setPath(image.getPath());
                    imageEntity.setSortOrder(image.getSortOrder());
                    imageEntity.setCreatedAt(image.getCreatedAt());
                    return imageEntity;
                })
                .collect(Collectors.toList());
        entity.setImages(images);
        return entity;
    }
}
