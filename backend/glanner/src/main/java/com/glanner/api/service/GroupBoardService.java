package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGroupBoardReqDto;

public interface GroupBoardService {
    void saveGroupBoard(String userEmail, SaveGroupBoardReqDto reqDto);
    void modifyGroupBoard(Long boardId, SaveGroupBoardReqDto reqDto);
}
