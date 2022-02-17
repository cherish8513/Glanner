package com.glanner.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class FindCommentResDto {
    private Long commentId;
    private Long parentId;
    private String userName;
    private String userEmail;
    private String content;
    private LocalDateTime createdDate;

    public FindCommentResDto(Long commentId, Long parentId, String userName, String userEmail, String content, LocalDateTime createdDate) {
        this.commentId = commentId;
        this.parentId = parentId != null ? parentId : -1;
        this.userName = userName;
        this.userEmail = userEmail;
        this.content = content;
        this.createdDate = createdDate;
    }
}
