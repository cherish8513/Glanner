package com.glanner.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindFreeBoardResDto extends FindBoardResDto {
    private int likeCount;
    private int dislikeCount;

    public FindFreeBoardResDto(Long boardId, String userEmail, String title, String content, int count, LocalDateTime createdDate, int likeCount, int dislikeCount) {
        super(boardId, userEmail, title, content, count, createdDate);
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }
}
