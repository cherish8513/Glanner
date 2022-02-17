package com.glanner.api.dto.response;

import com.glanner.core.domain.board.NoticeBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class FindNoticeBoardWithCommentResDto extends FindBoardResDto {
    List<FindCommentResDto> comments;

    public FindNoticeBoardWithCommentResDto(NoticeBoard noticeBoard, List<FindCommentResDto> comments){
        super(noticeBoard.getId(),
                noticeBoard.getUser().getName(),
                noticeBoard.getUser().getEmail(),
                noticeBoard.getTitle(),
                noticeBoard.getContent(),
                noticeBoard.getCount(),
                noticeBoard.getCreatedDate());
        this.comments = comments;
    }
}
