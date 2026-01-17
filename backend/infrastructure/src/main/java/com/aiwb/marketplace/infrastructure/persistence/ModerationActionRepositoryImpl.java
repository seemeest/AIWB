package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.ports.ModerationActionRepository;
import com.aiwb.marketplace.domain.moderation.ModerationAction;
import org.springframework.stereotype.Repository;

@Repository
public class ModerationActionRepositoryImpl implements ModerationActionRepository {
    private final JpaModerationActionRepository jpaRepository;

    public ModerationActionRepositoryImpl(JpaModerationActionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ModerationAction save(ModerationAction action) {
        jpaRepository.save(ModerationMapper.toEntity(action));
        return action;
    }
}
