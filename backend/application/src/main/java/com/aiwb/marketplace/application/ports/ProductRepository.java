package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.product.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
}
