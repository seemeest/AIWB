package com.aiwb.marketplace.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {
    Optional<RefreshTokenEntity> findByTokenAndRevokedFalse(String token);
}
