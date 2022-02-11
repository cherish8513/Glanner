package com.glanner.core.repository;

import com.glanner.core.domain.board.FreeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardCustomRepository {
    public Optional<FreeBoard> findByTitleLike(String title);
}
