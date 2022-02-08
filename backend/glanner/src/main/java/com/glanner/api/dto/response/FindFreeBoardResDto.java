package com.glanner.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindFreeBoardResDto extends FindBoardResDto {
    private int likeCount;
    private int dislikeCount;

    public FindFreeBoardResDto(String title, String content, int count, int likeCount, int dislikeCount) {
        super(title, content, count);
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }
}
