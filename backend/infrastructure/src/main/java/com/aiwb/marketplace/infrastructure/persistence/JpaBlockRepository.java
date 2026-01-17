package com.aiwb.marketplace.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaBlockRepository extends JpaRepository<BlockEntity, UUID> {
}
