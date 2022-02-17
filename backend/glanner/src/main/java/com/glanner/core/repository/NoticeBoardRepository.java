package com.glanner.core.repository;

import com.glanner.core.domain.board.NoticeBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long>, NoticeBoardCustomRepository {
    public Optional<NoticeBoard> findByTitleLike(String title);
}
