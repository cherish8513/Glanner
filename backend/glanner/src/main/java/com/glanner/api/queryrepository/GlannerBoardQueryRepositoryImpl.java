package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindGlannerBoardResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.glanner.core.domain.glanner.QGlannerBoard.glannerBoard;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GlannerBoardQueryRepositoryImpl implements GlannerBoardQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public List<FindGlannerBoardResDto> findPage(Long glannerId, int offset, int limit) {
        return query
                .select(Projections.constructor(FindGlannerBoardResDto.class,
                        glannerBoard.id,
                        glannerBoard.user.name,
                        glannerBoard.user.email,
                        glannerBoard.title,
                        glannerBoard.content,
                        glannerBoard.count,
                        glannerBoard.createdDate,
                        glannerBoard.comments.size()))
                .from(glannerBoard)
                .where(glannerBoard.glanner.id.eq(glannerId))
                .orderBy(glannerBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<FindGlannerBoardResDto> findPageWithKeyword(Long glannerId, int offset, int limit, String keyword) {
        return query
                .select(Projections.constructor(FindGlannerBoardResDto.class,
                        glannerBoard.id,
                        glannerBoard.user.name,
                        glannerBoard.user.email,
                        glannerBoard.title,
                        glannerBoard.content,
                        glannerBoard.count,
                        glannerBoard.createdDate,
                        glannerBoard.comments.size()))
                .from(glannerBoard)
                .where(glannerBoard.glanner.id.eq(glannerId)
                        .and((glannerBoard.title.contains(keyword).or(glannerBoard.content.contains(keyword)))))
                .orderBy(glannerBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
