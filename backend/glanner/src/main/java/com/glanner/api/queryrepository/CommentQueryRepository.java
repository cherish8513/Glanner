package com.glanner.api.queryrepository;

import com.glanner.api.dto.response.FindCommentResDto;

import java.util.List;

public interface CommentQueryRepository {
    List<FindCommentResDto> findCommentsByBoardId(Long boardId);
}
