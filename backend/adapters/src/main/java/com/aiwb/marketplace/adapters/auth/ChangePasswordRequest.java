package com.aiwb.marketplace.adapters.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChangePasswordRequest(@NotNull UUID userId,
                                    @NotBlank String currentPassword,
                                    @NotBlank String newPassword) {
}
