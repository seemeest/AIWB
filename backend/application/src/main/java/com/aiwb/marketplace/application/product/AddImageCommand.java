package com.aiwb.marketplace.application.product;

import java.io.InputStream;
import java.util.UUID;

public record AddImageCommand(UUID productId, UUID sellerId, String originalFilename, InputStream content) {
}
