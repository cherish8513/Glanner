package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGroupBoardReqDto;
import com.glanner.api.dto.response.FindGroupBoardWithCommentResDto;

public interface GroupBoardService {
    Long saveGroupBoard(String userEmail, SaveGroupBoardReqDto reqDto);
    void modifyGroupBoard(Long boardId, SaveGroupBoardReqDto reqDto);
    FindGroupBoardWithCommentResDto getGroupBoard(Long boardId);
}
