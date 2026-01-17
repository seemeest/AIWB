package com.aiwb.marketplace.adapters.product;

import com.aiwb.marketplace.application.metrics.MetricsService;
import com.aiwb.marketplace.application.product.AddImageCommand;
import com.aiwb.marketplace.application.product.CreateProductCommand;
import com.aiwb.marketplace.application.product.ProductService;
import com.aiwb.marketplace.domain.product.Product;
import com.aiwb.marketplace.domain.product.ProductImage;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final MetricsService metricsService;

    public ProductController(ProductService productService, MetricsService metricsService) {
        this.productService = productService;
        this.metricsService = metricsService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.create(new CreateProductCommand(
                request.sellerId(),
                request.title(),
                request.description(),
                request.price(),
                request.quantity()
        ));
        return ResponseEntity.ok(toResponse(product));
    }

    @PostMapping(path = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> uploadImage(@PathVariable("id") UUID productId,
                                                       @RequestParam("sellerId") UUID sellerId,
                                                       @RequestPart("file") MultipartFile file) throws IOException {
        Product product = productService.addImage(new AddImageCommand(
                productId,
                sellerId,
                file.getOriginalFilename(),
                file.getInputStream()
        ));
        return ResponseEntity.ok(toResponse(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable("id") UUID productId,
                                               @RequestParam(value = "viewerId", required = false) UUID viewerId,
                                               @RequestParam(value = "sessionId", required = false) String sessionId) {
        Product product = productService.getById(productId);
        metricsService.recordProductView(product.getId(), product.getSellerId(), viewerId, sessionId);
        return ResponseEntity.ok(toResponse(product));
    }

    private ProductResponse toResponse(Product product) {
        List<String> images = product.getImages().stream()
                .map(ProductImage::getPath)
                .toList();
        return new ProductResponse(
                product.getId(),
                product.getSellerId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getStatus().name(),
                images
        );
    }
}
