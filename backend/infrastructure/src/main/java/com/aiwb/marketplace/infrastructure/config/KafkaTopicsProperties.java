package com.aiwb.marketplace.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka.topics")
public class KafkaTopicsProperties {
    private String loginAudit = "login-audit";

    public String getLoginAudit() {
        return loginAudit;
    }

    public void setLoginAudit(String loginAudit) {
        this.loginAudit = loginAudit;
    }
}
