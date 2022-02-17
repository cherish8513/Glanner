package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindPlannerWorkResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.glanner.core.domain.user.QDailyWorkSchedule.dailyWorkSchedule;
import static com.glanner.core.domain.user.QUser.user;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryRepositoryImpl implements UserQueryRepository{
    private final JPAQueryFactory query;

    @Override
    public void deleteAllWorksByScheduleId(Long scheduleId){
        query
                .delete(dailyWorkSchedule)
                .where(dailyWorkSchedule.in(
                        JPAExpressions
                                .select(dailyWorkSchedule)
                                .from(dailyWorkSchedule)
                                .where(dailyWorkSchedule.schedule.id.eq(scheduleId))
                ))
                .execute();
    }

    @Override
    public List<FindPlannerWorkResDto> findDailyWorksWithPeriod(Long scheduleId, LocalDateTime start, LocalDateTime end) {
        return query
                .select(Projections.constructor(FindPlannerWorkResDto.class,
                        dailyWorkSchedule.id,
                        dailyWorkSchedule.title,
                        dailyWorkSchedule.content,
                        dailyWorkSchedule.startDate,
                        dailyWorkSchedule.endDate,
                        dailyWorkSchedule.alarmDate))
                .from(dailyWorkSchedule)
                .where(dailyWorkSchedule.schedule.id.eq(scheduleId),
                        dailyWorkSchedule.startDate.after(start),
                        dailyWorkSchedule.startDate.before(end))
                .fetch();
    }

    @Override
    public Optional<FindPlannerWorkResDto> findDailyWork(Long workId) {
        return Optional.ofNullable(query
                .select(Projections.constructor(FindPlannerWorkResDto.class,
                        dailyWorkSchedule.id,
                        dailyWorkSchedule.title,
                        dailyWorkSchedule.content,
                        dailyWorkSchedule.startDate,
                        dailyWorkSchedule.endDate,
                        dailyWorkSchedule.alarmDate))
                .from(dailyWorkSchedule)
                .where(dailyWorkSchedule.id.eq(workId))
                .fetchOne());
    }

    private BooleanExpression userEmailEq(String userEmail) {
        return userEmail != null ? user.email.eq(userEmail) : null;
    }
}
