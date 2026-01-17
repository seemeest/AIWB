package com.aiwb.marketplace.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaEmailVerificationTokenRepository extends JpaRepository<EmailVerificationTokenEntity, String> {
    Optional<EmailVerificationTokenEntity> findByToken(String token);
}
