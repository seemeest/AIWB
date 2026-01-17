package com.aiwb.marketplace.application.auth;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record RefreshToken(String token, UUID userId, Instant expiresAt, Instant createdAt, boolean revoked) {
    public RefreshToken {
        Objects.requireNonNull(token, "token");
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(expiresAt, "expiresAt");
        Objects.requireNonNull(createdAt, "createdAt");
    }
}
