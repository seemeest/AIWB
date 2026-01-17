package com.aiwb.marketplace.application.product;

import com.aiwb.marketplace.application.ports.ImageStorage;
import com.aiwb.marketplace.application.ports.ProductRepository;
import com.aiwb.marketplace.application.ports.ProductSearchIndex;
import com.aiwb.marketplace.domain.product.Product;
import com.aiwb.marketplace.domain.product.ProductImage;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

public class ProductService {
    private final ProductRepository productRepository;
    private final ImageStorage imageStorage;
    private final ProductSearchIndex productSearchIndex;
    private final Clock clock;

    public ProductService(ProductRepository productRepository,
                          ImageStorage imageStorage,
                          ProductSearchIndex productSearchIndex,
                          Clock clock) {
        this.productRepository = productRepository;
        this.imageStorage = imageStorage;
        this.productSearchIndex = productSearchIndex;
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
        Product saved = productRepository.save(product);
        productSearchIndex.index(saved);
        return saved;
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
        Product saved = productRepository.save(updated);
        productSearchIndex.index(saved);
        return saved;
    }

    public Product update(UpdateProductCommand command) {
        Product product = productRepository.findById(command.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (!product.getSellerId().equals(command.sellerId())) {
            throw new IllegalArgumentException("Seller mismatch");
        }

        String title = command.title() == null ? product.getTitle() : command.title();
        String description = command.description() == null ? product.getDescription() : command.description();
        java.math.BigDecimal price = command.price() == null ? product.getPrice() : command.price();
        int quantity = command.quantity() == null ? product.getQuantity() : command.quantity();

        Product updated = product.updateDetails(title, description, price, quantity, clock.instant());
        Product saved = productRepository.save(updated);
        productSearchIndex.index(saved);
        return saved;
    }

    public Product getById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }
}
