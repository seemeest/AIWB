package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.ports.ComplaintRepository;
import com.aiwb.marketplace.domain.moderation.Complaint;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ComplaintRepositoryImpl implements ComplaintRepository {
    private final JpaComplaintRepository jpaRepository;

    public ComplaintRepositoryImpl(JpaComplaintRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Complaint save(Complaint complaint) {
        ComplaintEntity saved = jpaRepository.save(ModerationMapper.toEntity(complaint));
        return ModerationMapper.toDomain(saved);
    }

    @Override
    public Optional<Complaint> findById(UUID id) {
        return jpaRepository.findById(id).map(ModerationMapper::toDomain);
    }
}
