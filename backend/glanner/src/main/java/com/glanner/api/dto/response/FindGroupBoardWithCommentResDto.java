package com.glanner.api.dto.response;

import com.glanner.core.domain.glanner.GroupBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class FindGroupBoardWithCommentResDto extends FindBoardResDto {
    private String interests;
    List<FindCommentResDto> comments;

    public FindGroupBoardWithCommentResDto(GroupBoard groupBoard, List<FindCommentResDto> comments){
        super(groupBoard.getId(),
                groupBoard.getUser().getName(),
                groupBoard.getUser().getEmail(),
                groupBoard.getTitle(),
                groupBoard.getContent(),
                groupBoard.getCount(),
                groupBoard.getCreatedDate());
        this.interests = groupBoard.getInterests();
        this.comments = comments;
    }
}
