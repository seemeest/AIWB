package com.aiwb.marketplace.adapters.moderation;

import com.aiwb.marketplace.domain.moderation.ModerationTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record DecisionRequest(
        @NotNull UUID moderatorId,
        @NotNull ModerationTargetType targetType,
        @NotNull UUID targetId,
        @NotBlank String action,
        @NotBlank String reason,
        Instant blockedUntil
) {
}
