package com.glanner.core.repository;

import com.glanner.core.domain.board.Comment;

import java.util.Optional;

public interface CommentCustomRepository {
    Optional<Comment> findRealById(Long id);
}
