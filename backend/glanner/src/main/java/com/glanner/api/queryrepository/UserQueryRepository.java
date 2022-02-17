package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindPlannerWorkResDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserQueryRepository {
    public void deleteAllWorksByScheduleId(Long scheduleId);
    public List<FindPlannerWorkResDto> findDailyWorksWithPeriod(Long scheduleId, LocalDateTime start, LocalDateTime end);
    public Optional<FindPlannerWorkResDto> findDailyWork(Long workId);
}
