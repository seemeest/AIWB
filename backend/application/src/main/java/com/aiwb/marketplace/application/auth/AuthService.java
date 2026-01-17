package com.aiwb.marketplace.application.auth;

import com.aiwb.marketplace.application.ports.EmailVerificationTokenRepository;
import com.aiwb.marketplace.application.ports.PasswordHasher;
import com.aiwb.marketplace.application.ports.RefreshTokenRepository;
import com.aiwb.marketplace.application.ports.TokenService;
import com.aiwb.marketplace.application.ports.UserRepository;
import com.aiwb.marketplace.application.notification.NotificationService;
import com.aiwb.marketplace.domain.notification.NotificationType;
import com.aiwb.marketplace.domain.user.RoleType;
import com.aiwb.marketplace.domain.user.User;
import com.aiwb.marketplace.domain.user.UserStatus;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailVerificationTokenRepository verificationTokenRepository;
    private final TokenService tokenService;
    private final PasswordHasher passwordHasher;
    private final Clock clock;
    private final Duration verificationTokenTtl;
    private final NotificationService notificationService;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       EmailVerificationTokenRepository verificationTokenRepository,
                       TokenService tokenService,
                       PasswordHasher passwordHasher,
                       Clock clock,
                       Duration verificationTokenTtl,
                       NotificationService notificationService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.tokenService = tokenService;
        this.passwordHasher = passwordHasher;
        this.clock = clock;
        this.verificationTokenTtl = verificationTokenTtl;
        this.notificationService = notificationService;
    }

    public RegistrationResult register(RegisterCommand command) {
        String email = normalizeEmail(command.email());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new AuthException(AuthError.EMAIL_ALREADY_USED, "Email already registered");
        }

        Set<RoleType> roles = command.roles();
        if (roles == null || roles.isEmpty()) {
            roles = Set.of(RoleType.BUYER);
        }

        User user = User.create(UUID.randomUUID(), email, passwordHasher.hash(command.password()), roles, clock.instant());
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        Instant now = clock.instant();
        EmailVerificationToken verificationToken =
                new EmailVerificationToken(token, user.getId(), now.plus(verificationTokenTtl), now);
        verificationTokenRepository.save(verificationToken);
        notificationService.notifyUser(user.getId(), NotificationType.REGISTRATION,
                "Регистрация",
                "Регистрация завершена. Подтвердите email по токену.",
                null);

        return new RegistrationResult(token);
    }

    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = verificationTokenRepository
                .findValidByToken(token)
                .orElseThrow(() -> new AuthException(AuthError.VERIFICATION_TOKEN_INVALID, "Verification token invalid"));

        User user = userRepository.findById(verificationToken.userId())
                .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND, "User not found"));

        userRepository.save(user.verifyEmail());
        verificationTokenRepository.delete(token);
        notificationService.notifyUser(user.getId(), NotificationType.EMAIL_VERIFIED,
                "Email подтвержден",
                "Email подтвержден. Теперь доступен вход в аккаунт.",
                null);
    }

    public AuthTokens login(LoginCommand command) {
        String email = normalizeEmail(command.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(AuthError.INVALID_CREDENTIALS, "Invalid credentials"));

        if (!passwordHasher.matches(command.password(), user.getPasswordHash())) {
            throw new AuthException(AuthError.INVALID_CREDENTIALS, "Invalid credentials");
        }
        if (user.getStatus() == UserStatus.PENDING_VERIFICATION) {
            throw new AuthException(AuthError.EMAIL_NOT_VERIFIED, "Email not verified");
        }
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new AuthException(AuthError.USER_BLOCKED, "User is blocked");
        }

        return issueTokens(user);
    }

    public AuthTokens refresh(RefreshCommand command) {
        RefreshToken existing = refreshTokenRepository.findValidByToken(command.refreshToken())
                .orElseThrow(() -> new AuthException(AuthError.INVALID_REFRESH_TOKEN, "Refresh token invalid"));

        User user = userRepository.findById(existing.userId())
                .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND, "User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AuthException(AuthError.USER_BLOCKED, "User is not active");
        }

        refreshTokenRepository.revokeByToken(existing.token());
        return issueTokens(user);
    }

    private AuthTokens issueTokens(User user) {
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        Instant now = clock.instant();
        RefreshToken stored = new RefreshToken(
                refreshToken,
                user.getId(),
                now.plus(tokenService.refreshTokenTtl()),
                now,
                false
        );
        refreshTokenRepository.save(stored);

        return new AuthTokens(accessToken, refreshToken);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
