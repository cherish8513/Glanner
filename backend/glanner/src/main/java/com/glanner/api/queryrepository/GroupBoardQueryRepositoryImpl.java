package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindGroupBoardResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.glanner.core.domain.glanner.QGroupBoard.groupBoard;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupBoardQueryRepositoryImpl implements GroupBoardQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public List<FindGroupBoardResDto> findPage(int offset, int limit) {
        return query
                .select(Projections.constructor(FindGroupBoardResDto.class,
                        groupBoard.id,
                        groupBoard.user.name,
                        groupBoard.user.email,
                        groupBoard.title,
                        groupBoard.content,
                        groupBoard.count,
                        groupBoard.createdDate,
                        groupBoard.interests,
                        groupBoard.comments.size(),
                        groupBoard.glanner.userGlanners.size()))
                .from(groupBoard)
                .orderBy(groupBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<FindGroupBoardResDto> findPageWithKeyword(int offset, int limit, String keyword) {
        return query
                .select(Projections.constructor(FindGroupBoardResDto.class,
                        groupBoard.id,
                        groupBoard.user.name,
                        groupBoard.user.email,
                        groupBoard.title,
                        groupBoard.content,
                        groupBoard.count,
                        groupBoard.createdDate,
                        groupBoard.interests,
                        groupBoard.comments.size(),
                        groupBoard.glanner.userGlanners.size()))
                .from(groupBoard)
                .where(groupBoard.title.contains(keyword)
                        .or(groupBoard.content.contains(keyword)))
                .orderBy(groupBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    @Override
    public List<FindGroupBoardResDto> findPageWithInterest(int offset, int limit, String interest) {
        return query
                .select(Projections.constructor(FindGroupBoardResDto.class,
                        groupBoard.id,
                        groupBoard.user.name,
                        groupBoard.title,
                        groupBoard.content,
                        groupBoard.count,
                        groupBoard.createdDate,
                        groupBoard.interests,
                        groupBoard.comments.size(),
                        groupBoard.glanner.userGlanners.size()))
                .from(groupBoard)
                .where(groupBoard.interests.contains(interest))
                .orderBy(groupBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
