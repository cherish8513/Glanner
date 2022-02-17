package com.glanner.api.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveGroupBoardResDto{
    Long groupBoardId;
    Long glannerId;
    String title;

    @Builder
    public SaveGroupBoardResDto(Long groupBoardId, Long glannerId, String title) {
        this.groupBoardId = groupBoardId;
        this.glannerId = glannerId;
        this.title = title;
    }
}
