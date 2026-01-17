package com.aiwb.marketplace.adapters.auth;

import com.aiwb.marketplace.application.auth.AuthService;
import com.aiwb.marketplace.application.auth.AuthTokens;
import com.aiwb.marketplace.application.auth.LoginCommand;
import com.aiwb.marketplace.application.auth.RefreshCommand;
import com.aiwb.marketplace.application.auth.RegisterCommand;
import com.aiwb.marketplace.application.auth.RegistrationResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthTokens tokens = authService.login(new LoginCommand(request.email(), request.password()));
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken(), tokens.refreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        AuthTokens tokens = authService.refresh(new RefreshCommand(request.refreshToken()));
        return ResponseEntity.ok(new AuthResponse(tokens.accessToken(), tokens.refreshToken()));
    }
}
