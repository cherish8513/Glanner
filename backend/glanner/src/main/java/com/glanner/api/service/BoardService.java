package com.glanner.api.service;

import com.glanner.api.dto.request.AddCommentReqDto;
import com.glanner.api.dto.request.SaveBoardReqDto;
import com.glanner.api.dto.request.UpdateCommentReqDto;


public interface BoardService {
    Long saveBoard(String userEmail, SaveBoardReqDto requestDto);
    void modifyBoard(Long boardId, SaveBoardReqDto requestDto);
    void deleteBoard(Long boardId);
    void addComment(String userEmail, AddCommentReqDto requestDto);
    void modifyComment(Long commentId, UpdateCommentReqDto requestDto);
    void deleteComment(Long commentId);
}
