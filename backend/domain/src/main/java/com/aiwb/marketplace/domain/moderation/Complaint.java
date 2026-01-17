package com.aiwb.marketplace.domain.moderation;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Complaint {
    private final UUID id;
    private final UUID authorId;
    private final UUID productId;
    private final String reason;
    private final ComplaintStatus status;
    private final Instant createdAt;

    private Complaint(UUID id,
                      UUID authorId,
                      UUID productId,
                      String reason,
                      ComplaintStatus status,
                      Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.authorId = Objects.requireNonNull(authorId, "authorId");
        this.productId = Objects.requireNonNull(productId, "productId");
        this.reason = Objects.requireNonNull(reason, "reason");
        this.status = Objects.requireNonNull(status, "status");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public static Complaint create(UUID id, UUID authorId, UUID productId, String reason, Instant createdAt) {
        return new Complaint(id, authorId, productId, reason, ComplaintStatus.OPEN, createdAt);
    }

    public static Complaint restore(UUID id,
                                    UUID authorId,
                                    UUID productId,
                                    String reason,
                                    ComplaintStatus status,
                                    Instant createdAt) {
        return new Complaint(id, authorId, productId, reason, status, createdAt);
    }

    public Complaint updateStatus(ComplaintStatus status) {
        return new Complaint(id, authorId, productId, reason, status, createdAt);
    }

    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getReason() {
        return reason;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
