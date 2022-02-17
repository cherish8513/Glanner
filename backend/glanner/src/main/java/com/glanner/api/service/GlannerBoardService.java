package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGlannerBoardReqDto;
import com.glanner.api.dto.response.FindGlannerBoardWithCommentsResDto;

import java.util.List;

public interface GlannerBoardService {
    List<FindGlannerBoardWithCommentsResDto> getGlannerBoards(Long glannerId, int offset, int limit);
    FindGlannerBoardWithCommentsResDto getGlannerBoard(Long boardId);
    Long saveGlannerBoard(String userEmail, SaveGlannerBoardReqDto reqDto);
}
