package com.glanner.api.queryrepository;

import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindGlannerBoardResDto;

import java.util.List;
import java.util.Optional;

public interface GlannerBoardQueryRepository {
    public Optional<FindGlannerBoardResDto> findById(Long id);
    public List<FindGlannerBoardResDto> findPage(Long glannerId, int offset, int limit);
    public List<FindGlannerBoardResDto> findByKeyWord(Long glannerId, int offset, int limit, SearchBoardReqDto reqDto);
}
