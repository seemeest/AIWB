package com.aiwb.marketplace.application.moderation;

public class ModerationException extends RuntimeException {
    private final ModerationError error;

    public ModerationException(ModerationError error, String message) {
        super(message);
        this.error = error;
    }

    public ModerationError getError() {
        return error;
    }
}
