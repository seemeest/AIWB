package com.aiwb.marketplace.application.auth;

import com.aiwb.marketplace.domain.user.RoleType;

import java.time.LocalDate;
import java.util.Set;

public record RegisterCommand(String email,
                              String password,
                              String fullName,
                              LocalDate birthDate,
                              Set<RoleType> roles) {
}
