package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGlannerBoardReqDto;

public interface GlannerBoardService {
    void saveGlannerBoard(String userEmail, SaveGlannerBoardReqDto reqDto);
}
