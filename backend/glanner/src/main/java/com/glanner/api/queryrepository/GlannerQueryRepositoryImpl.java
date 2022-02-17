package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindAttendedGlannerResDto;
import com.glanner.api.dto.response.FindGlannerWorkResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.glanner.core.domain.glanner.QDailyWorkGlanner.dailyWorkGlanner;
import static com.glanner.core.domain.glanner.QGlanner.glanner;
import static com.glanner.core.domain.glanner.QUserGlanner.userGlanner;
import static com.glanner.core.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GlannerQueryRepositoryImpl implements GlannerQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public List<FindGlannerWorkResDto> findDailyWorksDtoWithPeriod(Long id, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
        return query
                .select(Projections.constructor(FindGlannerWorkResDto.class,
                        dailyWorkGlanner.id,
                        dailyWorkGlanner.title,
                        dailyWorkGlanner.content,
                        dailyWorkGlanner.startDate,
                        dailyWorkGlanner.endDate,
                        dailyWorkGlanner.alarmDate))
                .from(dailyWorkGlanner)
                .where(dailyWorkGlanner.glanner.id.eq(id),
                        dailyWorkGlanner.startDate.after(dateTimeStart),
                        dailyWorkGlanner.startDate.before(dateTimeEnd))
                .fetch();
    }

    @Override
    public List<FindAttendedGlannerResDto> findAttendedGlannersDtoByUserId(Long userId) {
        return query
                .select(Projections.constructor(FindAttendedGlannerResDto.class,
                        glanner.id,
                        glanner.name,
                        user.email))
                .from(glanner)
                .join(glanner.userGlanners, userGlanner)
                .join(glanner.host, user)
                .where(userGlanner.user.id.eq(userId))
                .fetch();
    }


}
