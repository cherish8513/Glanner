package com.glanner.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindBoardResDto {
    private Long boardId;
    private String userName;
    private String userEmail;
    private String title;
    private String content;
    private int count;
    private LocalDateTime createdDate;
}
