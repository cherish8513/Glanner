package com.glanner.api.queryrepository;

import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindGroupBoardResDto;

import java.util.List;
import java.util.Optional;

public interface GroupBoardQueryRepository {
    public Optional<FindGroupBoardResDto> findById(Long id);
    public List<FindGroupBoardResDto> findPage(int offset, int limit);
    public List<FindGroupBoardResDto> findByKeyWord(int offset, int limit, SearchBoardReqDto reqDto);
    public List<FindGroupBoardResDto> findByInterest(int offset, int limit, SearchBoardReqDto reqDto);
}
