package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.application.auth.TokenPayload;
import com.aiwb.marketplace.domain.user.User;

import java.time.Duration;

public interface TokenService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    TokenPayload parseAccessToken(String token);
    Duration accessTokenTtl();
    Duration refreshTokenTtl();
}
