package com.aiwb.marketplace.application.product;

import com.aiwb.marketplace.application.ports.ImageStorage;
import com.aiwb.marketplace.application.ports.ProductRepository;
import com.aiwb.marketplace.domain.product.Product;
import com.aiwb.marketplace.domain.product.ProductImage;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public class ProductService {
    private final ProductRepository productRepository;
    private final ImageStorage imageStorage;
    private final Clock clock;

    public ProductService(ProductRepository productRepository, ImageStorage imageStorage, Clock clock) {
        this.productRepository = productRepository;
        this.imageStorage = imageStorage;
        this.clock = clock;
    }

    public Product create(CreateProductCommand command) {
        Instant now = clock.instant();
        Product product = Product.draft(
                UUID.randomUUID(),
                command.sellerId(),
                command.title(),
                command.description(),
                command.price(),
                command.quantity(),
                now
        );
        return productRepository.save(product);
    }

    public Product addImage(AddImageCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (!product.getSellerId().equals(command.sellerId())) {
            throw new IllegalArgumentException("Seller mismatch");
        }

        String filename = command.originalFilename() == null ? "image" : command.originalFilename();
        String directory = "products/" + product.getId();
        String storedPath = imageStorage.store(directory, filename, command.content());

        ProductImage image = new ProductImage(UUID.randomUUID(), storedPath, product.getImages().size(), clock.instant());
        Product updated = product.addImage(image);
        return productRepository.save(updated);
    }
}
