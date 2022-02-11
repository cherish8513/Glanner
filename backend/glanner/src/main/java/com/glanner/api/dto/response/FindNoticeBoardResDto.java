package com.glanner.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class FindNoticeBoardResDto extends FindBoardResDto {
    public FindNoticeBoardResDto(Long boardId, String userEmail, String title, String content, int count, LocalDateTime createdDate) {
        super(boardId, userEmail, title, content, count, createdDate);
    }
}
