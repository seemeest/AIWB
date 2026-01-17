package com.aiwb.marketplace.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.auth")
public class AuthProperties {
    private long verificationTokenTtlHours = 24;
    private long passwordResetTokenTtlHours = 2;

    public long getVerificationTokenTtlHours() {
        return verificationTokenTtlHours;
    }

    public void setVerificationTokenTtlHours(long verificationTokenTtlHours) {
        this.verificationTokenTtlHours = verificationTokenTtlHours;
    }

    public Duration verificationTokenTtl() {
        return Duration.ofHours(verificationTokenTtlHours);
    }

    public long getPasswordResetTokenTtlHours() {
        return passwordResetTokenTtlHours;
    }

    public void setPasswordResetTokenTtlHours(long passwordResetTokenTtlHours) {
        this.passwordResetTokenTtlHours = passwordResetTokenTtlHours;
    }

    public Duration passwordResetTokenTtl() {
        return Duration.ofHours(passwordResetTokenTtlHours);
    }
}
