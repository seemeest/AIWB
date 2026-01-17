package com.aiwb.marketplace.infrastructure.persistence;

import com.aiwb.marketplace.application.ports.UserRepository;
import com.aiwb.marketplace.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity saved = jpaUserRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id).map(UserMapper::toDomain);
    }
}
