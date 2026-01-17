package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.ports.LoginAuditRepository;
import com.aiwb.marketplace.domain.user.LoginAudit;
import org.springframework.stereotype.Repository;

@Repository
public class LoginAuditRepositoryImpl implements LoginAuditRepository {
    private final JpaLoginAuditRepository repository;

    public LoginAuditRepositoryImpl(JpaLoginAuditRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(LoginAudit audit) {
        repository.save(LoginAuditMapper.toEntity(audit));
    }
}
