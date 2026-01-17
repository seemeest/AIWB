package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.auth.RefreshToken;
import com.aiwb.marketplace.application.ports.RefreshTokenRepository;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final JpaRefreshTokenRepository jpaRepository;
    private final Clock clock;

    public RefreshTokenRepositoryImpl(JpaRefreshTokenRepository jpaRepository, Clock clock) {
        this.jpaRepository = jpaRepository;
        this.clock = clock;
    }

    @Override
    public RefreshToken save(RefreshToken token) {
        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setToken(token.token());
        entity.setUserId(token.userId());
        entity.setTokenVersion(token.tokenVersion());
        entity.setExpiresAt(token.expiresAt());
        entity.setCreatedAt(token.createdAt());
        entity.setRevoked(token.revoked());
        entity.setUserAgent(token.userAgent());
        entity.setIp(token.ip());
        entity.setDevice(token.device());
        entity.setBrowser(token.browser());
        RefreshTokenEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<RefreshToken> findValidByToken(String token) {
        Instant now = clock.instant();
        return jpaRepository.findByTokenAndRevokedFalse(token)
                .filter(entity -> entity.getExpiresAt().isAfter(now))
                .map(this::toDomain);
    }

    @Override
    public void revokeByToken(String token) {
        jpaRepository.findById(token).ifPresent(entity -> {
            entity.setRevoked(true);
            jpaRepository.save(entity);
        });
    }

    @Override
    public void revokeAllForUser(UUID userId) {
        jpaRepository.findAll().stream()
                .filter(entity -> userId.equals(entity.getUserId()))
                .forEach(entity -> {
                    entity.setRevoked(true);
                    jpaRepository.save(entity);
                });
    }

    private RefreshToken toDomain(RefreshTokenEntity entity) {
        return new RefreshToken(
                entity.getToken(),
                entity.getUserId(),
                entity.getTokenVersion(),
                entity.getExpiresAt(),
                entity.getCreatedAt(),
                entity.isRevoked(),
                entity.getUserAgent(),
                entity.getIp(),
                entity.getDevice(),
                entity.getBrowser()
        );
    }
}
