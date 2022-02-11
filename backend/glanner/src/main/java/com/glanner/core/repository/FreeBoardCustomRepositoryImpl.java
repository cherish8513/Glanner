package com.glanner.core.repository;

import com.glanner.core.domain.board.FreeBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.glanner.core.domain.board.QFreeBoard.freeBoard;
import static com.glanner.core.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FreeBoardCustomRepositoryImpl implements FreeBoardCustomRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<FreeBoard> findRealById(Long id) {
        return Optional.ofNullable(query
                .select(freeBoard)
                .from(freeBoard)
                .innerJoin(freeBoard.user, user)
                .where(freeBoard.id.eq(id))
                .fetchOne());
    }
}
