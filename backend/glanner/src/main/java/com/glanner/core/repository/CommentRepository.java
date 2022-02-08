package com.glanner.core.repository;

import com.glanner.core.domain.board.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
