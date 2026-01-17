package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.domain.moderation.Appeal;
import com.aiwb.marketplace.domain.moderation.Block;
import com.aiwb.marketplace.domain.moderation.Complaint;
import com.aiwb.marketplace.domain.moderation.ModerationAction;

public final class ModerationMapper {
    private ModerationMapper() {
    }

    public static Complaint toDomain(ComplaintEntity entity) {
        return Complaint.restore(
                entity.getId(),
                entity.getAuthorId(),
                entity.getProductId(),
                entity.getReason(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    public static ComplaintEntity toEntity(Complaint complaint) {
        ComplaintEntity entity = new ComplaintEntity();
        entity.setId(complaint.getId());
        entity.setAuthorId(complaint.getAuthorId());
        entity.setProductId(complaint.getProductId());
        entity.setReason(complaint.getReason());
        entity.setStatus(complaint.getStatus());
        entity.setCreatedAt(complaint.getCreatedAt());
        return entity;
    }

    public static ModerationActionEntity toEntity(ModerationAction action) {
        ModerationActionEntity entity = new ModerationActionEntity();
        entity.setId(action.getId());
        entity.setModeratorId(action.getModeratorId());
        entity.setTargetType(action.getTargetType());
        entity.setTargetId(action.getTargetId());
        entity.setAction(action.getAction());
        entity.setReason(action.getReason());
        entity.setCreatedAt(action.getCreatedAt());
        return entity;
    }

    public static Block toDomain(BlockEntity entity) {
        return Block.restore(
                entity.getId(),
                entity.getTargetType(),
                entity.getTargetId(),
                entity.getReason(),
                entity.getCreatedAt(),
                entity.getUntilAt()
        );
    }

    public static BlockEntity toEntity(Block block) {
        BlockEntity entity = new BlockEntity();
        entity.setId(block.getId());
        entity.setTargetType(block.getTargetType());
        entity.setTargetId(block.getTargetId());
        entity.setReason(block.getReason());
        entity.setCreatedAt(block.getCreatedAt());
        entity.setUntilAt(block.getUntilAt());
        return entity;
    }

    public static Appeal toDomain(AppealEntity entity) {
        return Appeal.restore(
                entity.getId(),
                entity.getBlockId(),
                entity.getAuthorId(),
                entity.getReason(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    public static AppealEntity toEntity(Appeal appeal) {
        AppealEntity entity = new AppealEntity();
        entity.setId(appeal.getId());
        entity.setBlockId(appeal.getBlockId());
        entity.setAuthorId(appeal.getAuthorId());
        entity.setReason(appeal.getReason());
        entity.setStatus(appeal.getStatus());
        entity.setCreatedAt(appeal.getCreatedAt());
        return entity;
    }
}
