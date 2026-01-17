package com.aiwb.marketplace.infrastructure.config;

import com.aiwb.marketplace.application.auth.AuthService;
import com.aiwb.marketplace.application.ports.EmailVerificationTokenRepository;
import com.aiwb.marketplace.application.ports.ImageStorage;
import com.aiwb.marketplace.application.ports.PasswordHasher;
import com.aiwb.marketplace.application.ports.RefreshTokenRepository;
import com.aiwb.marketplace.application.ports.TokenService;
import com.aiwb.marketplace.application.ports.UserRepository;
import com.aiwb.marketplace.application.ports.ProductRepository;
import com.aiwb.marketplace.application.product.ProductService;
import com.aiwb.marketplace.infrastructure.security.BCryptPasswordHasher;
import com.aiwb.marketplace.infrastructure.security.JwtTokenService;
import com.aiwb.marketplace.infrastructure.storage.LocalImageStorage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, AuthProperties.class, StorageProperties.class})
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
    public ImageStorage imageStorage(StorageProperties storageProperties) {
        return new LocalImageStorage(storageProperties.rootPath());
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

    @Bean
    public ProductService productService(ProductRepository productRepository,
                                         ImageStorage imageStorage,
                                         Clock clock) {
        return new ProductService(productRepository, imageStorage, clock);
    }
}
