package com.glanner.core.repository;

import com.glanner.core.domain.glanner.UserGlanner;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.glanner.core.domain.glanner.QUserGlanner.userGlanner;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserGlannerCustomRepositoryImpl implements UserGlannerCustomRepository{
    private final JPAQueryFactory query;

    @Override
    public List<UserGlanner> findByGlannerId(Long glannerId) {
        return query
                .select(userGlanner)
                .from(userGlanner)
                .where(userGlanner.glanner.id.eq(glannerId))
                .fetch();
    }

    @Override
    public UserGlanner findByUserIdAndGlannerId(Long userId, Long glannerId) {
        return query
                .select(userGlanner)
                .from(userGlanner)
                .where(userGlanner.glanner.id.eq(glannerId).and(
                        userGlanner.user.id.eq(userId)))
                .fetchOne();
    }
}
