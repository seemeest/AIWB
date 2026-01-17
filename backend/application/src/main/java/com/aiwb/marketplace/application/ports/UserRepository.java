package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
}
