package com.glanner.core.repository;

import com.glanner.core.domain.glanner.GlannerBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.glanner.core.domain.glanner.QGlanner.glanner;
import static com.glanner.core.domain.glanner.QGlannerBoard.glannerBoard;
import static com.glanner.core.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GlannerBoardCustomRepositoryImpl implements GlannerBoardCustomRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<GlannerBoard> findRealById(Long id) {
        return Optional.ofNullable(query
                .select(glannerBoard)
                .from(glannerBoard)
                .innerJoin(glannerBoard.user, user)
                .innerJoin(glannerBoard.glanner, glanner)
                .where(glannerBoard.id.eq(id))
                .fetchOne());
    }

    public List<GlannerBoard> findPage(Long glannerId, int offset, int limit) {
        return query
                .select(glannerBoard)
                .from(glannerBoard)
                .where(glannerBoard.glanner.id.eq(glannerId))
                .orderBy(glannerBoard.createdDate.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}
