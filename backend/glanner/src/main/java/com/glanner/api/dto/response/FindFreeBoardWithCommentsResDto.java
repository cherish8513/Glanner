package com.glanner.api.dto.response;

import com.glanner.core.domain.board.FreeBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class FindFreeBoardWithCommentsResDto extends FindBoardResDto {
    private int likeCount;
    private int dislikeCount;
    List<FindCommentResDto> comments;

    public FindFreeBoardWithCommentsResDto(FreeBoard freeBoard, List<FindCommentResDto> comments){
        super(freeBoard.getId(),
                freeBoard.getUser().getName(),
                freeBoard.getUser().getEmail(),
                freeBoard.getTitle(),
                freeBoard.getContent(),
                freeBoard.getCount(),
                freeBoard.getCreatedDate());
        likeCount = freeBoard.getLikeCount();
        dislikeCount = freeBoard.getDislikeCount();
        this.comments = comments;
    }
}
