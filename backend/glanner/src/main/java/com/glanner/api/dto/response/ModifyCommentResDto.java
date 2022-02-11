package com.glanner.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ModifyCommentResDto {
    private Long commentId;
    private Long parentId;
    private String userName;
    private String content;
    private LocalDateTime createdDate;

    public ModifyCommentResDto(Long commentId, Long parentId, String userName, String content, LocalDateTime createdDate) {
        this.commentId = commentId;
        this.parentId = parentId != null ? parentId : -1;
        this.userName = userName;
        this.content = content;
        this.createdDate = createdDate;
    }
}
