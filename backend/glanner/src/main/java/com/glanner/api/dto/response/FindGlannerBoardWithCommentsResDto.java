package com.glanner.api.dto.response;

import com.glanner.core.domain.glanner.GlannerBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class FindGlannerBoardWithCommentsResDto extends FindBoardResDto {
    private Long glannerId;
    private List<FindCommentResDto> comments;

    public FindGlannerBoardWithCommentsResDto(GlannerBoard glannerBoard, List<FindCommentResDto> comments){
        super(glannerBoard.getId(),
                glannerBoard.getUser().getName(),
                glannerBoard.getUser().getEmail(),
                glannerBoard.getTitle(),
                glannerBoard.getContent(),
                glannerBoard.getCount(),
                glannerBoard.getCreatedDate());
        glannerId = glannerBoard.getGlanner().getId();
        this.comments = comments;
    }

}
