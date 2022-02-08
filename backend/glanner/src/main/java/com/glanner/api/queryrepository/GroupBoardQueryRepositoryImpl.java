package com.glanner.api.queryrepository;

import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindGroupBoardResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.glanner.core.domain.glanner.QGroupBoard.groupBoard;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupBoardQueryRepositoryImpl implements GroupBoardQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<FindGroupBoardResDto> findById(Long id) {
        return Optional.ofNullable(query
                .select(Projections.constructor(FindGroupBoardResDto.class,
                        groupBoard.title,
                        groupBoard.content,
                        groupBoard.count,
                        groupBoard.interests))
                .from(groupBoard)
                .where(groupBoard.id.eq(id))
                .fetchOne());
    }

    @Override
    public List<FindGroupBoardResDto> findPage(int offset, int limit) {
        return query
                .select(Projections.constructor(FindGroupBoardResDto.class,
                        groupBoard.title,
                        groupBoard.content,
                        groupBoard.count,
                        groupBoard.interests))
                .from(groupBoard)
                .orderBy(groupBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<FindGroupBoardResDto> findByKeyWord(int offset, int limit, SearchBoardReqDto reqDto) {
        return query
                .select(Projections.constructor(FindGroupBoardResDto.class,
                        groupBoard.title,
                        groupBoard.content,
                        groupBoard.count,
                        groupBoard.interests))
                .from(groupBoard)
                .where(groupBoard.title.contains(reqDto.getKeyWord())
                        .or(groupBoard.content.contains(reqDto.getKeyWord())))
                .orderBy(groupBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<FindGroupBoardResDto> findByInterest(int offset, int limit, SearchBoardReqDto reqDto) {
        return query
                .select(Projections.constructor(FindGroupBoardResDto.class,
                        groupBoard.title,
                        groupBoard.content,
                        groupBoard.count,
                        groupBoard.interests))
                .from(groupBoard)
                .where(groupBoard.interests.contains(reqDto.getKeyWord()))
                .orderBy(groupBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
