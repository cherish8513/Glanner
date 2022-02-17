package com.glanner.core.repository;

import com.glanner.core.domain.glanner.GroupBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.glanner.core.domain.glanner.QGroupBoard.groupBoard;
import static com.glanner.core.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupBoardCustomRepositoryImpl implements GroupBoardCustomRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<GroupBoard> findRealById(Long id) {
        return Optional.ofNullable(query
                .select(groupBoard)
                .from(groupBoard)
                .innerJoin(groupBoard.user, user)
                .where(groupBoard.id.eq(id))
                .fetchOne());
    }

    @Override
    public Optional<GroupBoard> findByGlannerId(Long id) {
        return Optional.ofNullable(query
                .select(groupBoard)
                .from(groupBoard)
                .where(groupBoard.glanner.id.eq(id))
                .fetchOne());
    }
}
