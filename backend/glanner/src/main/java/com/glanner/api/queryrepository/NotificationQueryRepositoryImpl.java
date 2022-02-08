package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindWorkByTimeResDto;
import com.glanner.core.domain.user.NotificationStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.glanner.core.domain.glanner.QDailyWorkGlanner.dailyWorkGlanner;
import static com.glanner.core.domain.glanner.QUserGlanner.userGlanner;
import static com.glanner.core.domain.user.QDailyWorkSchedule.dailyWorkSchedule;
import static com.glanner.core.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public List<FindWorkByTimeResDto> findWorkBySchedule() {
        LocalDateTime now = LocalDateTime.now();

        return query
                .select(Projections.constructor(FindWorkByTimeResDto.class,
                        dailyWorkSchedule.id,
                        dailyWorkSchedule.title,
                        user.phoneNumber))
                .from(dailyWorkSchedule)
                .innerJoin(user)
                .on(dailyWorkSchedule.schedule.eq(user.schedule))
                .where(dailyWorkSchedule.notiDate.before(now)
                        .and(dailyWorkSchedule.notiStatus.eq(NotificationStatus.STILL_NOT_CONFIRMED)))
                .fetch();
    }

    @Override
    public List<FindWorkByTimeResDto> findWorkByGlanner() {
        LocalDateTime now = LocalDateTime.now();

        return query
                .select(Projections.constructor(FindWorkByTimeResDto.class,
                        dailyWorkGlanner.id,
                        dailyWorkGlanner.title,
                        user.phoneNumber))
                .from(user)
                .join(userGlanner).on(userGlanner.user.eq(user))
                .join(dailyWorkGlanner).on(dailyWorkGlanner.glanner.eq(userGlanner.glanner))
                .where(dailyWorkGlanner.notiDate.before(now)
                        .and(dailyWorkGlanner.notiStatus.eq(NotificationStatus.STILL_NOT_CONFIRMED)))
                .fetch();
    }
}
