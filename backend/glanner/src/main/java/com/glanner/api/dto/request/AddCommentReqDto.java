package com.glanner.api.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddCommentReqDto {
    @NotNull
    private Long boardId;
    @NotNull
    private String content;
    private Long parentId;
}
