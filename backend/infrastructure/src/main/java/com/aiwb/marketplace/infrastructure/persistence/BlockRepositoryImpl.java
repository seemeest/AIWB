package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.ports.BlockQueryRepository;
import com.aiwb.marketplace.application.ports.BlockRepository;
import com.aiwb.marketplace.domain.moderation.Block;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BlockRepositoryImpl implements BlockRepository, BlockQueryRepository {
    private final JpaBlockRepository jpaRepository;

    public BlockRepositoryImpl(JpaBlockRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Block save(Block block) {
        BlockEntity saved = jpaRepository.save(ModerationMapper.toEntity(block));
        return ModerationMapper.toDomain(saved);
    }

    @Override
    public Optional<Instant> findCreatedAt(UUID blockId) {
        return jpaRepository.findById(blockId).map(BlockEntity::getCreatedAt);
    }
}
