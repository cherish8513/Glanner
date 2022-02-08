package com.glanner.api.queryrepository;

import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindNoticeBoardResDto;

import java.util.List;
import java.util.Optional;

public interface NoticeBoardQueryRepository {
    public Optional<FindNoticeBoardResDto> findById(Long id);
    public List<FindNoticeBoardResDto> findPage(int offset, int limit);
    public List<FindNoticeBoardResDto> findByKeyWord(int offset, int limit, SearchBoardReqDto reqDto);
}
