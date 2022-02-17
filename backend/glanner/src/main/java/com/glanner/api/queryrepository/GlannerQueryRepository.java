package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindAttendedGlannerResDto;
import com.glanner.api.dto.response.FindGlannerWorkResDto;

import java.time.LocalDateTime;
import java.util.List;

public interface GlannerQueryRepository {
    List<FindGlannerWorkResDto> findDailyWorksDtoWithPeriod(Long id, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd);
    List<FindAttendedGlannerResDto> findAttendedGlannersDtoByUserId(Long userId);
}
