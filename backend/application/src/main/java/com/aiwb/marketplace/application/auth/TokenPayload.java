package com.aiwb.marketplace.application.auth;

import com.aiwb.marketplace.domain.user.RoleType;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record TokenPayload(UUID userId, String email, Set<RoleType> roles) {
    public TokenPayload {
        Objects.requireNonNull(userId, "userId");
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(roles, "roles");
    }
}
