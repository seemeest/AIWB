package com.aiwb.marketplace.domain.moderation;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Block {
    private final UUID id;
    private final ModerationTargetType targetType;
    private final UUID targetId;
    private final String reason;
    private final Instant createdAt;
    private final Instant untilAt;

    private Block(UUID id,
                  ModerationTargetType targetType,
                  UUID targetId,
                  String reason,
                  Instant createdAt,
                  Instant untilAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.targetType = Objects.requireNonNull(targetType, "targetType");
        this.targetId = Objects.requireNonNull(targetId, "targetId");
        this.reason = Objects.requireNonNull(reason, "reason");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.untilAt = untilAt;
    }

    public static Block create(UUID id,
                               ModerationTargetType targetType,
                               UUID targetId,
                               String reason,
                               Instant createdAt,
                               Instant untilAt) {
        return new Block(id, targetType, targetId, reason, createdAt, untilAt);
    }

    public static Block restore(UUID id,
                                ModerationTargetType targetType,
                                UUID targetId,
                                String reason,
                                Instant createdAt,
                                Instant untilAt) {
        return new Block(id, targetType, targetId, reason, createdAt, untilAt);
    }

    public UUID getId() {
        return id;
    }

    public ModerationTargetType getTargetType() {
        return targetType;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public String getReason() {
        return reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUntilAt() {
        return untilAt;
    }
}
