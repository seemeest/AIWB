package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.moderation.Complaint;

import java.util.Optional;
import java.util.UUID;

public interface ComplaintRepository {
    Complaint save(Complaint complaint);
    Optional<Complaint> findById(UUID id);
}
