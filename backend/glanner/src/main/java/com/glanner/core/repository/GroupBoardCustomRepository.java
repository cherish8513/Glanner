package com.glanner.core.repository;

import com.glanner.core.domain.glanner.GroupBoard;

import java.util.Optional;

public interface GroupBoardCustomRepository {
    Optional<GroupBoard> findRealById(Long id);
    Optional<GroupBoard> findByGlannerId(Long id);
}
