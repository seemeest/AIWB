package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.auth.EmailVerificationToken;
import com.aiwb.marketplace.application.ports.EmailVerificationTokenRepository;
import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Repository
public class EmailVerificationTokenRepositoryImpl implements EmailVerificationTokenRepository {
    private final JpaEmailVerificationTokenRepository jpaRepository;
    private final Clock clock;

    public EmailVerificationTokenRepositoryImpl(JpaEmailVerificationTokenRepository jpaRepository, Clock clock) {
        this.jpaRepository = jpaRepository;
        this.clock = clock;
    }

    @Override
    public EmailVerificationToken save(EmailVerificationToken token) {
        EmailVerificationTokenEntity entity = new EmailVerificationTokenEntity();
        entity.setToken(token.token());
        entity.setUserId(token.userId());
        entity.setExpiresAt(token.expiresAt());
        entity.setCreatedAt(token.createdAt());
        EmailVerificationTokenEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<EmailVerificationToken> findValidByToken(String token) {
        Instant now = clock.instant();
        return jpaRepository.findByToken(token)
                .filter(entity -> entity.getExpiresAt().isAfter(now))
                .map(this::toDomain);
    }

    @Override
    public void delete(String token) {
        jpaRepository.deleteById(token);
    }

    private EmailVerificationToken toDomain(EmailVerificationTokenEntity entity) {
        return new EmailVerificationToken(
                entity.getToken(),
                entity.getUserId(),
                entity.getExpiresAt(),
                entity.getCreatedAt()
        );
    }
}
