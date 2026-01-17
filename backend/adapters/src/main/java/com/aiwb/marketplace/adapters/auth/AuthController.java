package com.aiwb.marketplace.adapters.auth;

import com.aiwb.marketplace.application.auth.AuthService;
import com.aiwb.marketplace.application.auth.AuthTokens;
import com.aiwb.marketplace.application.auth.ChangePasswordCommand;
import com.aiwb.marketplace.application.auth.LoginCommand;
import com.aiwb.marketplace.application.auth.RefreshCommand;
import com.aiwb.marketplace.application.auth.RegisterCommand;
import com.aiwb.marketplace.application.auth.RegistrationResult;
import com.aiwb.marketplace.application.auth.RequestPasswordResetCommand;
import com.aiwb.marketplace.application.auth.ResetPasswordCommand;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegistrationResult result = authService.register(
                new RegisterCommand(request.email(), request.password(), request.roles())
        );
        return ResponseEntity.ok(new RegistrationResponse(result.verificationToken()));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request.token());
        return ResponseEntity.ok(new MessageResponse("Email verified"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest httpRequest) {
        String userAgent = headerOrNull(httpRequest, "User-Agent");
        String ip = resolveClientIp(httpRequest);
        String device = headerOrNull(httpRequest, "X-Device");
        String browser = headerOrNull(httpRequest, "X-Browser");
        AuthTokens tokens = authService.login(new LoginCommand(
                request.email(),
                request.password(),
                userAgent,
                ip,
                device,
                browser
        ));
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken(), tokens.refreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        AuthTokens tokens = authService.refresh(new RefreshCommand(request.refreshToken()));
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken(), tokens.refreshToken()));
    }

    @PostMapping("/password/reset-request")
    public ResponseEntity<MessageResponse> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        authService.requestPasswordReset(new RequestPasswordResetCommand(request.email()));
        return ResponseEntity.ok(new MessageResponse("Password reset requested"));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody PasswordResetConfirmRequest request) {
        authService.resetPassword(new ResetPasswordCommand(request.token(), request.newPassword()));
        return ResponseEntity.ok(new MessageResponse("Password reset completed"));
    }

    @PostMapping("/password/change")
    public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(new ChangePasswordCommand(
                request.userId(),
                request.currentPassword(),
                request.newPassword()
        ));
        return ResponseEntity.ok(new MessageResponse("Password changed"));
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = headerOrNull(request, "X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        String realIp = headerOrNull(request, "X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

    private String headerOrNull(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        return value == null || value.isBlank() ? null : value.trim();
    }
}
