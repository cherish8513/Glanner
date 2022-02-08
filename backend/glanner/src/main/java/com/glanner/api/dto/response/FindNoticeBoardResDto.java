package com.glanner.api.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FindNoticeBoardResDto extends FindBoardResDto {
    public FindNoticeBoardResDto(String title, String content, int count) {
        super(title, content, count);
    }
}
