package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindFreeBoardResDto;

import java.util.List;

public interface FreeBoardQueryRepository {
    public List<FindFreeBoardResDto> findPage(int offset, int limit);
    public List<FindFreeBoardResDto> findPageWithKeyword(int offset, int limit, String keyword);
}
