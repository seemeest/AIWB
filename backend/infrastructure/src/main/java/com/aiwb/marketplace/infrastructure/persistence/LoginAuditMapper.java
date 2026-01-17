package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.domain.user.LoginAudit;

public final class LoginAuditMapper {
    private LoginAuditMapper() {
    }

    public static LoginAudit toDomain(LoginAuditEntity entity) {
        if (entity == null) {
            return null;
        }
        return LoginAudit.create(
                entity.getUserId(),
                entity.getLastLoginAt(),
                entity.getUserAgent(),
                entity.getIp(),
                entity.getDevice(),
                entity.getBrowser(),
                entity.getCountry(),
                entity.getRegion(),
                entity.getCity()
        );
    }

    public static LoginAuditEntity toEntity(LoginAudit audit) {
        LoginAuditEntity entity = new LoginAuditEntity();
        entity.setUserId(audit.getUserId());
        entity.setLastLoginAt(audit.getLastLoginAt());
        entity.setUserAgent(audit.getUserAgent());
        entity.setIp(audit.getIp());
        entity.setDevice(audit.getDevice());
        entity.setBrowser(audit.getBrowser());
        entity.setCountry(audit.getCountry());
        entity.setRegion(audit.getRegion());
        entity.setCity(audit.getCity());
        return entity;
    }
}
