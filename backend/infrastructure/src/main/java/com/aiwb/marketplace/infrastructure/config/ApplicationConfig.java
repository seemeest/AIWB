package com.aiwb.marketplace.infrastructure.config;

import com.aiwb.marketplace.application.auth.AuthService;
import com.aiwb.marketplace.application.ports.EmailVerificationTokenRepository;
import com.aiwb.marketplace.application.ports.PasswordHasher;
import com.aiwb.marketplace.application.ports.RefreshTokenRepository;
import com.aiwb.marketplace.application.ports.TokenService;
import com.aiwb.marketplace.application.ports.UserRepository;
import com.aiwb.marketplace.infrastructure.security.BCryptPasswordHasher;
import com.aiwb.marketplace.infrastructure.security.JwtTokenService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, AuthProperties.class})
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public PasswordHasher passwordHasher() {
        return new BCryptPasswordHasher();
    }

    @Bean
    public TokenService tokenService(JwtProperties jwtProperties, Clock clock) {
        return new JwtTokenService(jwtProperties, clock);
    }

    @Bean
    public AuthService authService(UserRepository userRepository,
                                   RefreshTokenRepository refreshTokenRepository,
                                   EmailVerificationTokenRepository verificationTokenRepository,
                                   TokenService tokenService,
                                   PasswordHasher passwordHasher,
                                   Clock clock,
                                   AuthProperties authProperties) {
        return new AuthService(
                userRepository,
                refreshTokenRepository,
                verificationTokenRepository,
                tokenService,
                passwordHasher,
                clock,
                authProperties.verificationTokenTtl()
        );
    }
}
