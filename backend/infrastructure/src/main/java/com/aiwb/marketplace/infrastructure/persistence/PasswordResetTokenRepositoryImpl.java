package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.auth.PasswordResetToken;
import com.aiwb.marketplace.application.ports.PasswordResetTokenRepository;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Repository
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {
    private final JpaPasswordResetTokenRepository repository;
    private final Clock clock;

    public PasswordResetTokenRepositoryImpl(JpaPasswordResetTokenRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        entity.setToken(token.token());
        entity.setUserId(token.userId());
        entity.setExpiresAt(token.expiresAt());
        entity.setCreatedAt(token.createdAt());
        PasswordResetTokenEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<PasswordResetToken> findValidByToken(String token) {
        Instant now = clock.instant();
        return repository.findById(token)
                .filter(entity -> entity.getExpiresAt().isAfter(now))
                .map(this::toDomain);
    }

    @Override
    public void delete(String token) {
        repository.deleteById(token);
    }

    private PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
        return new PasswordResetToken(entity.getToken(), entity.getUserId(), entity.getExpiresAt(), entity.getCreatedAt());
    }
}
