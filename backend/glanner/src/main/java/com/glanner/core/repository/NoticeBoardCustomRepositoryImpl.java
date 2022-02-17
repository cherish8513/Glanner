package com.glanner.core.repository;

import com.glanner.core.domain.board.NoticeBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.glanner.core.domain.board.QNoticeBoard.noticeBoard;
import static com.glanner.core.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeBoardCustomRepositoryImpl implements NoticeBoardCustomRepository{

    private final JPAQueryFactory query;
    
    @Override
    public Optional<NoticeBoard> findRealById(Long id) {
        return Optional.ofNullable(query
                .select(noticeBoard)
                .from(noticeBoard)
                .innerJoin(noticeBoard.user, user)
                .where(noticeBoard.id.eq(id))
                .fetchOne());
    }
}
