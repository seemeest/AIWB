package com.aiwb.marketplace.application.auth;

public record ResetPasswordCommand(String token, String newPassword) {
}
