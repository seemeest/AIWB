package com.aiwb.marketplace.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "moderation")
public class ModerationProperties {
    private long appealWindowDays = 7;

    public long getAppealWindowDays() {
        return appealWindowDays;
    }

    public void setAppealWindowDays(long appealWindowDays) {
        this.appealWindowDays = appealWindowDays;
    }

    public Duration appealWindow() {
        return Duration.ofDays(appealWindowDays);
    }
}
