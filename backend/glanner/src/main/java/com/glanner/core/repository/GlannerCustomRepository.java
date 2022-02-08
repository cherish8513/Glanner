package com.glanner.core.repository;

import com.glanner.core.domain.glanner.Glanner;

import java.util.Optional;

public interface GlannerCustomRepository {
    Optional<Glanner> findRealById(Long id);
    void deleteAllWorksById(Long id);
    void deleteAllUserGlannerById(Long id);
}
