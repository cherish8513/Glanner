package com.glanner.api.queryrepository;

import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindGlannerBoardResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.glanner.core.domain.glanner.QGlannerBoard.glannerBoard;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GlannerBoardQueryRepositoryImpl implements GlannerBoardQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<FindGlannerBoardResDto> findById(Long id) {
        return Optional.ofNullable(query
                .select(Projections.constructor(FindGlannerBoardResDto.class,
                        glannerBoard.title,
                        glannerBoard.content,
                        glannerBoard.count))
                .from(glannerBoard)
                .where(glannerBoard.id.eq(id))
                .fetchOne());
    }

    @Override
    public List<FindGlannerBoardResDto> findPage(Long glannerId, int offset, int limit) {
        return query
                .select(Projections.constructor(FindGlannerBoardResDto.class,
                        glannerBoard.title,
                        glannerBoard.content,
                        glannerBoard.count))
                .from(glannerBoard)
                .where(glannerBoard.glanner.id.eq(glannerId))
                .orderBy(glannerBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<FindGlannerBoardResDto> findByKeyWord(Long glannerId, int offset, int limit, SearchBoardReqDto reqDto) {
        return query
                .select(Projections.constructor(FindGlannerBoardResDto.class,
                        glannerBoard.title,
                        glannerBoard.content,
                        glannerBoard.count))
                .from(glannerBoard)
                .where(glannerBoard.glanner.id.eq(glannerId)
                        .and((glannerBoard.title.contains(reqDto.getKeyWord()).or(glannerBoard.content.contains(reqDto.getKeyWord())))))
                .orderBy(glannerBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
