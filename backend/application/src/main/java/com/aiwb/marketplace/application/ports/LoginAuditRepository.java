package com.aiwb.marketplace.application.ports;

import com.aiwb.marketplace.domain.user.LoginAudit;

public interface LoginAuditRepository {
    void save(LoginAudit audit);

    java.util.Optional<LoginAudit> findByUserId(java.util.UUID userId);
}
