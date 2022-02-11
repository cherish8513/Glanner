package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGlannerBoardReqDto;
import com.glanner.api.dto.response.FindGlannerBoardWithCommentsResDto;

public interface GlannerBoardService {
    FindGlannerBoardWithCommentsResDto getGlannerBoard(Long boardId);
    Long saveGlannerBoard(String userEmail, SaveGlannerBoardReqDto reqDto);
}
