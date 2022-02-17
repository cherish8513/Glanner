package com.glanner.core.repository;

import com.glanner.core.domain.board.FreeBoard;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GlannerBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GlannerBoardRepository extends JpaRepository<GlannerBoard, Long>, GlannerBoardCustomRepository {
    public void deleteByGlanner(Glanner glanner);
}