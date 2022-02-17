package com.glanner.api.service;

import com.glanner.api.dto.request.AddCommentReqDto;
import com.glanner.api.dto.request.SaveBoardReqDto;
import com.glanner.api.dto.request.UpdateCommentReqDto;
import com.glanner.api.dto.response.ModifyCommentResDto;
import com.glanner.api.dto.response.SaveCommentResDto;


public interface BoardService {
    Long saveBoard(String userEmail, SaveBoardReqDto requestDto);
    void modifyBoard(Long boardId, SaveBoardReqDto requestDto);
    void deleteBoard(Long boardId);
    SaveCommentResDto addComment(String userEmail, AddCommentReqDto requestDto);
    ModifyCommentResDto modifyComment(Long commentId, UpdateCommentReqDto requestDto);
    void deleteComment(Long commentId);
}
