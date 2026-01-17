package com.aiwb.marketplace.application.auth;

import com.aiwb.marketplace.domain.user.RoleType;

import java.util.Set;

public record RegisterCommand(String email, String password, Set<RoleType> roles) {
}
