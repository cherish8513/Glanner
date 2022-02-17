package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindNotificationResDto;
import com.glanner.api.dto.response.FindWorkByTimeResDto;

import java.util.List;

public interface NotificationQueryRepository {
    public List<FindNotificationResDto> findNotificationResDtoByUserId(Long userId);
    public List<FindNotificationResDto> findUnreadNotificationResDtoByUserId(Long userId);
    public List<FindWorkByTimeResDto> findScheduleWork();
    public List<FindWorkByTimeResDto> findGlannerWork();
    public List<FindWorkByTimeResDto> findReservedConference();
}
