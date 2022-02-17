package com.glanner.core.repository;

import com.glanner.core.domain.glanner.GlannerBoard;

import java.util.List;
import java.util.Optional;

public interface GlannerBoardCustomRepository {
    Optional<GlannerBoard> findRealById(Long id);
    List<GlannerBoard> findPage(Long glannerId, int offset, int limit);
}
