package com.aiwb.marketplace.application.auth;

import com.aiwb.marketplace.application.ports.EmailVerificationTokenRepository;
import com.aiwb.marketplace.application.ports.LoginAuditRepository;
import com.aiwb.marketplace.application.ports.PasswordHasher;
import com.aiwb.marketplace.application.ports.PasswordResetTokenRepository;
import com.aiwb.marketplace.application.ports.RefreshTokenRepository;
import com.aiwb.marketplace.application.ports.TokenService;
import com.aiwb.marketplace.application.ports.UserRepository;
import com.aiwb.marketplace.application.notification.NotificationService;
import com.aiwb.marketplace.domain.notification.NotificationType;
import com.aiwb.marketplace.domain.user.LoginAudit;
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
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final LoginAuditRepository loginAuditRepository;
    private final TokenService tokenService;
    private final PasswordHasher passwordHasher;
    private final Clock clock;
    private final Duration verificationTokenTtl;
    private final Duration passwordResetTokenTtl;
    private final NotificationService notificationService;

    public AuthService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       EmailVerificationTokenRepository verificationTokenRepository,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       LoginAuditRepository loginAuditRepository,
                       TokenService tokenService,
                       PasswordHasher passwordHasher,
                       Clock clock,
                       Duration verificationTokenTtl,
                       Duration passwordResetTokenTtl,
                       NotificationService notificationService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.loginAuditRepository = loginAuditRepository;
        this.tokenService = tokenService;
        this.passwordHasher = passwordHasher;
        this.clock = clock;
        this.verificationTokenTtl = verificationTokenTtl;
        this.passwordResetTokenTtl = passwordResetTokenTtl;
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

        loginAuditRepository.save(LoginAudit.create(
                user.getId(),
                clock.instant(),
                command.userAgent(),
                command.ip(),
                command.device(),
                command.browser(),
                null,
                null,
                null
        ));

        return issueTokens(user, command.userAgent(), command.ip(), command.device(), command.browser());
    }

    public AuthTokens refresh(RefreshCommand command) {
        RefreshToken existing = refreshTokenRepository.findValidByToken(command.refreshToken())
                .orElseThrow(() -> new AuthException(AuthError.INVALID_REFRESH_TOKEN, "Refresh token invalid"));

        User user = userRepository.findById(existing.userId())
                .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND, "User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new AuthException(AuthError.USER_BLOCKED, "User is not active");
        }
        if (existing.tokenVersion() != user.getTokenVersion()) {
            refreshTokenRepository.revokeByToken(existing.token());
            throw new AuthException(AuthError.INVALID_REFRESH_TOKEN, "Refresh token invalid");
        }

        refreshTokenRepository.revokeByToken(existing.token());
        return issueTokens(user, null, null, null, null);
    }

    public void requestPasswordReset(RequestPasswordResetCommand command) {
        String email = normalizeEmail(command.email());
        userRepository.findByEmail(email).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            Instant now = clock.instant();
            PasswordResetToken resetToken = new PasswordResetToken(
                    token,
                    user.getId(),
                    now.plus(passwordResetTokenTtl),
                    now
            );
            passwordResetTokenRepository.save(resetToken);
            notificationService.notifyUser(user.getId(), NotificationType.PASSWORD_RESET,
                    "Сброс пароля",
                    "Запрошен сброс пароля. Токен: " + token,
                    null);
        });
    }

    public void resetPassword(ResetPasswordCommand command) {
        PasswordResetToken token = passwordResetTokenRepository.findValidByToken(command.token())
                .orElseThrow(() -> new AuthException(AuthError.PASSWORD_RESET_TOKEN_INVALID, "Reset token invalid"));

        User user = userRepository.findById(token.userId())
                .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND, "User not found"));

        User updated = user.changePassword(passwordHasher.hash(command.newPassword()));
        userRepository.save(updated);
        refreshTokenRepository.revokeAllForUser(user.getId());
        passwordResetTokenRepository.delete(token.token());
        notificationService.notifyUser(user.getId(), NotificationType.PASSWORD_CHANGED,
                "Пароль изменен",
                "Пароль успешно изменен.",
                null);
    }

    public void changePassword(ChangePasswordCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND, "User not found"));
        if (!passwordHasher.matches(command.currentPassword(), user.getPasswordHash())) {
            throw new AuthException(AuthError.INVALID_CREDENTIALS, "Invalid credentials");
        }
        User updated = user.changePassword(passwordHasher.hash(command.newPassword()));
        userRepository.save(updated);
        refreshTokenRepository.revokeAllForUser(user.getId());
        notificationService.notifyUser(user.getId(), NotificationType.PASSWORD_CHANGED,
                "Пароль изменен",
                "Пароль успешно изменен.",
                null);
    }

    private AuthTokens issueTokens(User user, String userAgent, String ip, String device, String browser) {
        String accessToken = tokenService.generateAccessToken(user);
        String refreshToken = tokenService.generateRefreshToken(user);

        Instant now = clock.instant();
        RefreshToken stored = new RefreshToken(
                refreshToken,
                user.getId(),
                user.getTokenVersion(),
                now.plus(tokenService.refreshTokenTtl()),
                now,
                false,
                userAgent,
                ip,
                device,
                browser
        );
        refreshTokenRepository.save(stored);

        return new AuthTokens(accessToken, refreshToken);
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
