package com.glanner.core.repository;

import com.glanner.core.domain.glanner.GlannerBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlannerBoardRepository extends JpaRepository<GlannerBoard, Long>, GlannerBoardCustomRepository {
}