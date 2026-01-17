package com.aiwb.marketplace.adapters.moderation;

import java.util.UUID;

public record ComplaintResponse(UUID id, UUID authorId, UUID productId, String status, String reason) {
}
