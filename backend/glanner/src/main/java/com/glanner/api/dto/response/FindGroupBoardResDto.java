package com.glanner.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindGroupBoardResDto extends FindBoardResDto {
    private String interests;

    public FindGroupBoardResDto(Long boardId, String userEmail, String title, String content, int count, LocalDateTime createdDate, String interests) {
        super(boardId, userEmail, title, content, count, createdDate);
        this.interests = interests;
    }
}
