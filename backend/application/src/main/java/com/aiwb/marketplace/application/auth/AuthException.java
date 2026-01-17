package com.aiwb.marketplace.application.auth;

public class AuthException extends RuntimeException {
    private final AuthError error;

    public AuthException(AuthError error, String message) {
        super(message);
        this.error = error;
    }

    public AuthError getError() {
        return error;
    }
}
