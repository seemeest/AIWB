package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.moderation.Appeal;

import java.util.Optional;
import java.util.UUID;

public interface AppealRepository {
    Appeal save(Appeal appeal);
    Optional<Appeal> findById(UUID id);
}
