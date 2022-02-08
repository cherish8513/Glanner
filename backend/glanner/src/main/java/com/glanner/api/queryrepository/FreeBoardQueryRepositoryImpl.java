package com.glanner.api.queryrepository;

import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindFreeBoardResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.glanner.core.domain.board.QFreeBoard.freeBoard;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FreeBoardQueryRepositoryImpl implements FreeBoardQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<FindFreeBoardResDto> findById(Long id) {
        return Optional.ofNullable(query
                .select(Projections.constructor(FindFreeBoardResDto.class,
                        freeBoard.title,
                        freeBoard.content,
                        freeBoard.count,
                        freeBoard.likeCount,
                        freeBoard.dislikeCount))
                .from(freeBoard)
                .where(freeBoard.id.eq(id))
                .fetchOne());
    }

    @Override
    public List<FindFreeBoardResDto> findPage(int offset, int limit) {
        return query
                .select(Projections.constructor(FindFreeBoardResDto.class,
                        freeBoard.title,
                        freeBoard.content,
                        freeBoard.count,
                        freeBoard.likeCount,
                        freeBoard.dislikeCount))
                .from(freeBoard)
                .orderBy(freeBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<FindFreeBoardResDto> findByKeyWord(int offset, int limit, SearchBoardReqDto reqDto) {
        return query
                .select(Projections.constructor(FindFreeBoardResDto.class,
                        freeBoard.title,
                        freeBoard.content,
                        freeBoard.count,
                        freeBoard.likeCount,
                        freeBoard.dislikeCount))
                .from(freeBoard)
                .where(freeBoard.title.contains(reqDto.getKeyWord())
                        .or(freeBoard.content.contains(reqDto.getKeyWord())))
                .orderBy(freeBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
