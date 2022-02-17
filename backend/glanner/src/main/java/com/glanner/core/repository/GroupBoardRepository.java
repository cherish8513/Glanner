package com.glanner.core.repository;

import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GroupBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupBoardRepository extends JpaRepository<GroupBoard, Long>, GroupBoardCustomRepository {
    public void deleteByGlanner(Glanner glanner);
}