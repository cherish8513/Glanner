package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindGlannerWorkResDto;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyWorkGlannerQueryRepository {
    List<FindGlannerWorkResDto> findByGlannerIdWithDate(Long glannerId, LocalDateTime startDate, LocalDateTime endDate);
}
