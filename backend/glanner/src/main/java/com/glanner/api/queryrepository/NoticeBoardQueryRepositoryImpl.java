package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindNoticeBoardResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.glanner.core.domain.board.QNoticeBoard.noticeBoard;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeBoardQueryRepositoryImpl implements NoticeBoardQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public List<FindNoticeBoardResDto> findPage(int offset, int limit) {
        return query
                .select(Projections.constructor(FindNoticeBoardResDto.class,
                        noticeBoard.id,
                        noticeBoard.user.name,
                        noticeBoard.user.email,
                        noticeBoard.title,
                        noticeBoard.content,
                        noticeBoard.count,
                        noticeBoard.createdDate,
                        noticeBoard.comments.size()))
                .from(noticeBoard)
                .orderBy(noticeBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<FindNoticeBoardResDto> findPageWithKeyword(int offset, int limit, String keyword) {
        return query
                .select(Projections.constructor(FindNoticeBoardResDto.class,
                        noticeBoard.id,
                        noticeBoard.user.name,
                        noticeBoard.user.email,
                        noticeBoard.title,
                        noticeBoard.content,
                        noticeBoard.count,
                        noticeBoard.createdDate,
                        noticeBoard.comments.size()))
                .from(noticeBoard)
                .where(noticeBoard.title.contains(keyword)
                        .or(noticeBoard.content.contains(keyword)))
                .orderBy(noticeBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
