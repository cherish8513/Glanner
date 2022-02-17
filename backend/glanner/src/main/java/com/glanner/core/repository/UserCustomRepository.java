package com.glanner.core.repository;

import com.glanner.core.domain.user.User;

import java.util.Optional;

public interface UserCustomRepository {
    public Optional<User> findByEmail(String email);
}
