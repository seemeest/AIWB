package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.application.auth.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken token);
    Optional<RefreshToken> findValidByToken(String token);
    void revokeByToken(String token);
    void revokeAllForUser(java.util.UUID userId);
}
