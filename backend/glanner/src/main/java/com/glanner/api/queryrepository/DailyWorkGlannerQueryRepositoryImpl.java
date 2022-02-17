package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindGlannerWorkResDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.glanner.core.domain.glanner.QDailyWorkGlanner.dailyWorkGlanner;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DailyWorkGlannerQueryRepositoryImpl implements DailyWorkGlannerQueryRepository{

    private final JPAQueryFactory query;

    @Override
    public List<FindGlannerWorkResDto> findByGlannerIdWithDate(Long glannerId, LocalDateTime startDate, LocalDateTime endDate) {
        return query
                .select(Projections.constructor(FindGlannerWorkResDto.class,
                        dailyWorkGlanner.title,
                        dailyWorkGlanner.content,
                        dailyWorkGlanner.startDate,
                        dailyWorkGlanner.endDate,
                        dailyWorkGlanner.alarmDate))

                .from(dailyWorkGlanner)
                .where(dailyWorkGlanner.glanner.id.eq(glannerId),
                        dailyWorkGlanner.startDate.after(startDate),
                        dailyWorkGlanner.startDate.before(endDate))
                .orderBy(dailyWorkGlanner.startDate.desc())
                .fetch();
    }


}
