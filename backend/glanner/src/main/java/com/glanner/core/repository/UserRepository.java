package com.glanner.core.repository;

import com.glanner.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository{
    public Optional<User> findByEmail(String email);
}
