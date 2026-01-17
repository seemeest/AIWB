package com.aiwb.marketplace.domain.moderation;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class ModerationAction {
    private final UUID id;
    private final UUID moderatorId;
    private final ModerationTargetType targetType;
    private final UUID targetId;
    private final String action;
    private final String reason;
    private final Instant createdAt;

    private ModerationAction(UUID id,
                             UUID moderatorId,
                             ModerationTargetType targetType,
                             UUID targetId,
                             String action,
                             String reason,
                             Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.moderatorId = Objects.requireNonNull(moderatorId, "moderatorId");
        this.targetType = Objects.requireNonNull(targetType, "targetType");
        this.targetId = Objects.requireNonNull(targetId, "targetId");
        this.action = Objects.requireNonNull(action, "action");
        this.reason = Objects.requireNonNull(reason, "reason");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public static ModerationAction create(UUID id,
                                          UUID moderatorId,
                                          ModerationTargetType targetType,
                                          UUID targetId,
                                          String action,
                                          String reason,
                                          Instant createdAt) {
        return new ModerationAction(id, moderatorId, targetType, targetId, action, reason, createdAt);
    }

    public static ModerationAction restore(UUID id,
                                           UUID moderatorId,
                                           ModerationTargetType targetType,
                                           UUID targetId,
                                           String action,
                                           String reason,
                                           Instant createdAt) {
        return new ModerationAction(id, moderatorId, targetType, targetId, action, reason, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getModeratorId() {
        return moderatorId;
    }

    public ModerationTargetType getTargetType() {
        return targetType;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public String getAction() {
        return action;
    }

    public String getReason() {
        return reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
