package com.glanner.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindGroupBoardResDto extends FindBoardResDto {
    private String interests;

    public FindGroupBoardResDto(String title, String content, int count, String interests) {
        super(title, content, count);
        this.interests = interests;
    }
}
