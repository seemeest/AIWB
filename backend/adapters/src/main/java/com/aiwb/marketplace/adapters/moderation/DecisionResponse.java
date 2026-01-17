package com.aiwb.marketplace.adapters.moderation;

import java.util.UUID;

public record DecisionResponse(UUID id, String targetType, UUID targetId, String action, String reason) {
}
