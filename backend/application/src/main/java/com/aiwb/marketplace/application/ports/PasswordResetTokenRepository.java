package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.application.auth.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository {
    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findValidByToken(String token);
    void delete(String token);
}
