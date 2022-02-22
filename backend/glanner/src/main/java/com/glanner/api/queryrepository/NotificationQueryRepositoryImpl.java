package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindNotificationResDto;
import com.glanner.api.dto.response.FindWorkByTimeResDto;
import com.glanner.core.domain.user.ConfirmStatus;
import com.glanner.core.domain.user.QSchedule;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.glanner.core.domain.glanner.QDailyWorkGlanner.dailyWorkGlanner;
import static com.glanner.core.domain.glanner.QGlanner.glanner;
import static com.glanner.core.domain.glanner.QUserGlanner.userGlanner;
import static com.glanner.core.domain.user.QDailyWorkSchedule.dailyWorkSchedule;
import static com.glanner.core.domain.user.QNotification.notification;
import static com.glanner.core.domain.user.QSchedule.*;
import static com.glanner.core.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public List<FindNotificationResDto> findNotificationResDtoByUserId(Long userId){
        return query
                .select(Projections.constructor(FindNotificationResDto.class,
                        notification.type,
                        notification.typeId,
                        notification.confirmation,
                        notification.content,
                        notification.createdDate))
                .from(notification)
                .join(notification.user, user)
                .where(user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<FindNotificationResDto> findUnreadNotificationResDtoByUserId(Long userId) {
        return query
                .select(Projections.constructor(FindNotificationResDto.class,
                        notification.type,
                        notification.typeId,
                        notification.confirmation,
                        notification.content,
                        notification.createdDate))
                .from(notification)
                .join(notification.user, user)
                .where(user.id.eq(userId)
                        .and(notification.confirmation.eq(ConfirmStatus.STILL_NOT_CONFIRMED)))
                .fetch();
    }


    @Override
    public List<FindWorkByTimeResDto> findScheduleWork() {
        return query
                .select(Projections.constructor(FindWorkByTimeResDto.class,
                        dailyWorkSchedule.id,
                        dailyWorkSchedule.title,
                        user.id,
                        user.phoneNumber))
                .from(dailyWorkSchedule)
                .join(dailyWorkSchedule.schedule, schedule)
                .join(schedule.user, user)
                .where(dailyWorkSchedule.confirmStatus.eq(ConfirmStatus.STILL_NOT_CONFIRMED))
                .fetch();
    }

    @Override
    public List<FindWorkByTimeResDto> findGlannerWork() {
        return query
                .select(Projections.constructor(FindWorkByTimeResDto.class,
                        dailyWorkGlanner.id,
                        dailyWorkGlanner.title,
                        user.id,
                        user.phoneNumber))
                .from(user)
                .join(user.userGlanners, userGlanner)
                .join(userGlanner.glanner, glanner)
                .join(glanner.works, dailyWorkGlanner)
                .where(dailyWorkGlanner.confirmStatus.eq(ConfirmStatus.STILL_NOT_CONFIRMED))
                .fetch();
    }

    @Override
    public List<FindWorkByTimeResDto> findReservedConference() {

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(1);

        return query
                .select(Projections.constructor(FindWorkByTimeResDto.class,
                        dailyWorkGlanner.id,
                        dailyWorkGlanner.title,
                        user.id,
                        user.phoneNumber))
                .from(user)
                .join(user.userGlanners, userGlanner)
                .join(userGlanner.glanner, glanner)
                .join(glanner.works, dailyWorkGlanner)
                .where(dailyWorkGlanner.startDate.between(start, end))
                .fetch();
    }
}
