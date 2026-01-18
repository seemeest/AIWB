package com.aiwb.marketplace.adapters.auth;

import com.aiwb.marketplace.domain.user.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.Set;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String fullName,
        @NotNull @Past LocalDate birthDate,
        Set<RoleType> roles
) {
}
