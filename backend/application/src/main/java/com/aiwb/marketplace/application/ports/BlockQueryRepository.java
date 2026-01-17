package com.aiwb.marketplace.application.ports;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface BlockQueryRepository {
    Optional<Instant> findCreatedAt(UUID blockId);
}
