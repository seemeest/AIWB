package com.aiwb.marketplace.adapters.moderation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AppealRequest(
        @NotNull UUID blockId,
        @NotNull UUID authorId,
        @NotBlank String reason
) {
}
