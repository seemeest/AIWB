package com.aiwb.marketplace.application.moderation;

import java.util.UUID;

public record CreateComplaintCommand(UUID authorId, UUID productId, String reason) {
}
