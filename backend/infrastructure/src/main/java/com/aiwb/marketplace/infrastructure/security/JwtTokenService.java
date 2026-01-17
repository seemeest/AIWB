package com.aiwb.marketplace.infrastructure.security;

import com.aiwb.marketplace.application.auth.AuthError;
import com.aiwb.marketplace.application.auth.AuthException;
import com.aiwb.marketplace.application.auth.TokenPayload;
import com.aiwb.marketplace.application.ports.TokenService;
import com.aiwb.marketplace.domain.user.RoleType;
import com.aiwb.marketplace.domain.user.User;
import com.aiwb.marketplace.infrastructure.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtTokenService implements TokenService {
    private final JwtProperties properties;
    private final Clock clock;
    private final SecretKey key;

    public JwtTokenService(JwtProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(User user) {
        Instant now = clock.instant();
        Instant expiresAt = now.plus(accessTokenTtl());
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("uid", user.getId().toString())
                .claim("roles", user.getRoles().stream().map(RoleType::name).toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        Instant now = clock.instant();
        Instant expiresAt = now.plus(refreshTokenTtl());
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("uid", user.getId().toString())
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
    }

    @Override
    public TokenPayload parseAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String userId = claims.get("uid", String.class);
            List<String> roles = claims.get("roles", List.class);
            Set<RoleType> roleTypes = roles == null ? Set.of() : roles.stream()
                    .map(RoleType::valueOf)
                    .collect(Collectors.toSet());
            return new TokenPayload(UUID.fromString(userId), claims.getSubject(), roleTypes);
        } catch (ExpiredJwtException ex) {
            throw new AuthException(AuthError.TOKEN_EXPIRED, "Access token expired");
        } catch (JwtException | IllegalArgumentException ex) {
            throw new AuthException(AuthError.INVALID_ACCESS_TOKEN, "Access token invalid");
        }
    }

    @Override
    public Duration accessTokenTtl() {
        return Duration.ofMinutes(properties.getAccessTokenTtlMinutes());
    }

    @Override
    public Duration refreshTokenTtl() {
        return Duration.ofDays(properties.getRefreshTokenTtlDays());
    }
}
