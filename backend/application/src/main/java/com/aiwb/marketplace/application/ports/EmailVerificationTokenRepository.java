package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.application.auth.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenRepository {
    EmailVerificationToken save(EmailVerificationToken token);
    Optional<EmailVerificationToken> findValidByToken(String token);
    void delete(String token);
}
