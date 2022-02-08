package com.glanner.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FindGlannerBoardResDto extends FindBoardResDto {
    public FindGlannerBoardResDto(String title, String content, int count) {
        super(title, content, count);
    }
}
