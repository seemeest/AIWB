package com.aiwb.marketplace.adapters.moderation;

import java.util.UUID;

public record AppealResponse(UUID id, UUID blockId, UUID authorId, String status, String reason) {
}
