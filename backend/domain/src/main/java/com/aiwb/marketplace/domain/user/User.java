package com.aiwb.marketplace.domain.user;

import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class User {
    private final UUID id;
    private final String email;
    private final String passwordHash;
    private final UserStatus status;
    private final Set<RoleType> roles;
    private final Instant createdAt;
    private final int tokenVersion;

    private User(UUID id,
                 String email,
                 String passwordHash,
                 UserStatus status,
                 Set<RoleType> roles,
                 Instant createdAt,
                 int tokenVersion) {
        this.id = Objects.requireNonNull(id, "id");
        this.email = Objects.requireNonNull(email, "email");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash");
        this.status = Objects.requireNonNull(status, "status");
        this.roles = Collections.unmodifiableSet(Objects.requireNonNull(roles, "roles"));
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.tokenVersion = tokenVersion;
    }

    public static User create(UUID id, String email, String passwordHash, Set<RoleType> roles, Instant createdAt) {
        return new User(id, email, passwordHash, UserStatus.PENDING_VERIFICATION, roles, createdAt, 0);
    }

    public static User restore(UUID id,
                               String email,
                               String passwordHash,
                               UserStatus status,
                               Set<RoleType> roles,
                               Instant createdAt,
                               int tokenVersion) {
        return new User(id, email, passwordHash, status, roles, createdAt, tokenVersion);
    }

    public User verifyEmail() {
        return new User(id, email, passwordHash, UserStatus.ACTIVE, roles, createdAt, tokenVersion);
    }

    public User block() {
        return new User(id, email, passwordHash, UserStatus.BLOCKED, roles, createdAt, tokenVersion);
    }

    public User changePassword(String passwordHash) {
        return new User(id, email, passwordHash, status, roles, createdAt, tokenVersion + 1);
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Set<RoleType> getRoles() {
        return roles;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public int getTokenVersion() {
        return tokenVersion;
    }
}
