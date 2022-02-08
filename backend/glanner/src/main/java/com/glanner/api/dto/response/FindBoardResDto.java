package com.glanner.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindBoardResDto {
    private String title;
    private String content;
    private int count;
}
