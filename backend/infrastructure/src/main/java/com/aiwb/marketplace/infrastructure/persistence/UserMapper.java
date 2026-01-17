package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.domain.user.User;

public final class UserMapper {
    private UserMapper() {
    }

    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.restore(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getStatus(),
                entity.getRoles(),
                entity.getCreatedAt(),
                entity.getTokenVersion()
        );
    }

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setStatus(user.getStatus());
        entity.setRoles(user.getRoles());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setTokenVersion(user.getTokenVersion());
        return entity;
    }
}
