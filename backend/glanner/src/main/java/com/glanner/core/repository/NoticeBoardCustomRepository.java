package com.glanner.core.repository;

import com.glanner.core.domain.board.NoticeBoard;

import java.util.Optional;

public interface NoticeBoardCustomRepository {
    Optional<NoticeBoard> findRealById(Long id);
}
