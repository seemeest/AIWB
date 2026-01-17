package com.aiwb.marketplace.application.auth;

public record LoginCommand(String email,
                           String password,
                           String userAgent,
                           String ip,
                           String device,
                           String browser) {
}
