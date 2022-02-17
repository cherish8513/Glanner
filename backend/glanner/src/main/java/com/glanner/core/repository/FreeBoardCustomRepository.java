package com.glanner.core.repository;

import com.glanner.core.domain.board.FreeBoard;

import java.util.Optional;

public interface FreeBoardCustomRepository {
    Optional<FreeBoard> findRealById(Long id);
}
