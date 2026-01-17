package com.aiwb.marketplace.adapters.moderation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ComplaintRequest(
        @NotNull UUID authorId,
        @NotNull UUID productId,
        @NotBlank String reason
) {
}
