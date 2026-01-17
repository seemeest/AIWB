package com.aiwb.marketplace.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaModerationActionRepository extends JpaRepository<ModerationActionEntity, UUID> {
}
