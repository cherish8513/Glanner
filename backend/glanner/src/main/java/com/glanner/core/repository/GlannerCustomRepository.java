package com.glanner.core.repository;

import com.glanner.core.domain.glanner.Glanner;

import java.util.Optional;

public interface GlannerCustomRepository {
    public Optional<Glanner> findRealById(Long id);
}
