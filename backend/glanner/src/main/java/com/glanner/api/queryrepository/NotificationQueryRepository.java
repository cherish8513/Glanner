package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindWorkByTimeResDto;

import java.util.List;

public interface NotificationQueryRepository {
    public List<FindWorkByTimeResDto> findWorkBySchedule();
    public List<FindWorkByTimeResDto> findWorkByGlanner();
}
