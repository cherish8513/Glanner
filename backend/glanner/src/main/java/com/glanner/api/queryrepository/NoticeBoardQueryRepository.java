package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindNoticeBoardResDto;

import java.util.List;

public interface NoticeBoardQueryRepository {
    public List<FindNoticeBoardResDto> findPage(int offset, int limit);
    public List<FindNoticeBoardResDto> findPageWithKeyword(int offset, int limit, String keyword);
}
