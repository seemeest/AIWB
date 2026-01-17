package com.aiwb.marketplace.application.moderation;

import com.aiwb.marketplace.domain.moderation.ModerationTargetType;

import java.time.Instant;
import java.util.UUID;

public record ModerationDecisionCommand(
        UUID moderatorId,
        ModerationTargetType targetType,
        UUID targetId,
        String action,
        String reason,
        Instant blockedUntil
) {
}
