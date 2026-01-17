package com.aiwb.marketplace.adapters.auth;

import com.aiwb.marketplace.domain.user.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        Set<RoleType> roles
) {
}
