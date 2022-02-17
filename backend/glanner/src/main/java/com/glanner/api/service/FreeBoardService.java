package com.glanner.api.service;

import com.glanner.api.dto.response.FindFreeBoardWithCommentsResDto;

public interface FreeBoardService {
    FindFreeBoardWithCommentsResDto getFreeBoard(Long boardId);
    void addLike(Long boardId);
    void addDislike(Long boardId);
}
