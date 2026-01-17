package com.aiwb.marketplace.domain.user;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class LoginAudit {
    private final UUID userId;
    private final Instant lastLoginAt;
    private final String userAgent;
    private final String ip;
    private final String device;
    private final String browser;
    private final String country;
    private final String region;
    private final String city;

    private LoginAudit(UUID userId,
                       Instant lastLoginAt,
                       String userAgent,
                       String ip,
                       String device,
                       String browser,
                       String country,
                       String region,
                       String city) {
        this.userId = Objects.requireNonNull(userId, "userId");
        this.lastLoginAt = Objects.requireNonNull(lastLoginAt, "lastLoginAt");
        this.userAgent = userAgent;
        this.ip = ip;
        this.device = device;
        this.browser = browser;
        this.country = country;
        this.region = region;
        this.city = city;
    }

    public static LoginAudit create(UUID userId,
                                    Instant lastLoginAt,
                                    String userAgent,
                                    String ip,
                                    String device,
                                    String browser,
                                    String country,
                                    String region,
                                    String city) {
        return new LoginAudit(userId, lastLoginAt, userAgent, ip, device, browser, country, region, city);
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIp() {
        return ip;
    }

    public String getDevice() {
        return device;
    }

    public String getBrowser() {
        return browser;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }
}
