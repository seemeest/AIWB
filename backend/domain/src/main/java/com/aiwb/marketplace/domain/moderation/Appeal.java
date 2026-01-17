package com.aiwb.marketplace.domain.moderation;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Appeal {
    private final UUID id;
    private final UUID blockId;
    private final UUID authorId;
    private final String reason;
    private final AppealStatus status;
    private final Instant createdAt;

    private Appeal(UUID id,
                   UUID blockId,
                   UUID authorId,
                   String reason,
                   AppealStatus status,
                   Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.blockId = Objects.requireNonNull(blockId, "blockId");
        this.authorId = Objects.requireNonNull(authorId, "authorId");
        this.reason = Objects.requireNonNull(reason, "reason");
        this.status = Objects.requireNonNull(status, "status");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public static Appeal create(UUID id, UUID blockId, UUID authorId, String reason, Instant createdAt) {
        return new Appeal(id, blockId, authorId, reason, AppealStatus.SUBMITTED, createdAt);
    }

    public static Appeal restore(UUID id,
                                 UUID blockId,
                                 UUID authorId,
                                 String reason,
                                 AppealStatus status,
                                 Instant createdAt) {
        return new Appeal(id, blockId, authorId, reason, status, createdAt);
    }

    public Appeal updateStatus(AppealStatus status) {
        return new Appeal(id, blockId, authorId, reason, status, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getBlockId() {
        return blockId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getReason() {
        return reason;
    }

    public AppealStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
