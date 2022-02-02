package com.glanner.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateCommentReqDto {
    @NotNull
    private Long commentId;
    @NotNull
    private String content;
}
