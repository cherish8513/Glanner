package com.glanner.core.repository;

import com.glanner.core.domain.glanner.Glanner;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.glanner.core.domain.board.QComment.comment;
import static com.glanner.core.domain.glanner.QDailyWorkGlanner.dailyWorkGlanner;
import static com.glanner.core.domain.glanner.QGlanner.glanner;
import static com.glanner.core.domain.glanner.QGlannerBoard.glannerBoard;
import static com.glanner.core.domain.glanner.QGroupBoard.groupBoard;
import static com.glanner.core.domain.glanner.QUserGlanner.userGlanner;
import static com.glanner.core.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GlannerCustomRepositoryImpl implements GlannerCustomRepository{

    private final JPAQueryFactory query;

    @Override
    public Optional<Glanner> findRealById(Long id) {
        return Optional.ofNullable(query
                .select(glanner)
                .from(glanner)
                .innerJoin(glanner.host, user).fetchJoin()
                .leftJoin(glanner.userGlanners, userGlanner)
                .leftJoin(glanner.glannerBoards, glannerBoard)
                .leftJoin(glanner.works, dailyWorkGlanner)
                .where(glanner.id.eq(id))
                .fetchOne());
    }

    @Override
    @Transactional
    public void deleteAllWorksById(Long id) {
        query
                .delete(dailyWorkGlanner)
                .where(dailyWorkGlanner.in(
                        JPAExpressions
                                .select(dailyWorkGlanner)
                                .from(dailyWorkGlanner)
                                .where(dailyWorkGlanner.glanner.id.eq(id))
                ))
                .execute();
    }

    @Override
    @Transactional
    public void deleteAllUserGlannerById(Long id) {
        query
                .delete(userGlanner)
                .where(userGlanner.in(
                        JPAExpressions
                                .select(userGlanner)
                                .from(userGlanner)
                                .where(userGlanner.glanner.id.eq(id))
                ))
                .execute();
    }
}
