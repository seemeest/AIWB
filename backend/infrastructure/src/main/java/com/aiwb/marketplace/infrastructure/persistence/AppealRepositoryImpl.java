package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.ports.AppealRepository;
import com.aiwb.marketplace.domain.moderation.Appeal;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AppealRepositoryImpl implements AppealRepository {
    private final JpaAppealRepository jpaRepository;

    public AppealRepositoryImpl(JpaAppealRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Appeal save(Appeal appeal) {
        AppealEntity saved = jpaRepository.save(ModerationMapper.toEntity(appeal));
        return ModerationMapper.toDomain(saved);
    }

    @Override
    public Optional<Appeal> findById(UUID id) {
        return jpaRepository.findById(id).map(ModerationMapper::toDomain);
    }
}
