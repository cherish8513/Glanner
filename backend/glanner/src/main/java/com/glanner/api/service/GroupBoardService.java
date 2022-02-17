package com.glanner.api.service;

import com.glanner.api.dto.request.SaveGroupBoardReqDto;
import com.glanner.api.dto.response.FindGlannerResDto;
import com.glanner.api.dto.response.FindGroupBoardWithCommentResDto;
import com.glanner.api.dto.response.SaveGroupBoardResDto;

public interface GroupBoardService {
    SaveGroupBoardResDto saveGroupBoard(String userEmail, SaveGroupBoardReqDto reqDto);
    void modifyGroupBoard(Long boardId, SaveGroupBoardReqDto reqDto);
    FindGroupBoardWithCommentResDto getGroupBoard(Long boardId);
    FindGlannerResDto getGlannerDetail(Long boardId);
}
