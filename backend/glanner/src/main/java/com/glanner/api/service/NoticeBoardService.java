package com.glanner.api.service;

import com.glanner.api.dto.response.FindNoticeBoardWithCommentResDto;

public interface NoticeBoardService {
    FindNoticeBoardWithCommentResDto getNotice(Long boardId);
}
