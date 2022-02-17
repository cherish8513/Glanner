package com.glanner.api.dto.request;

import com.glanner.core.domain.glanner.GroupBoard;
import com.glanner.core.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@NoArgsConstructor
@Getter
public class SaveGroupBoardReqDto extends SaveBoardReqDto {
    public String interests;

    public SaveGroupBoardReqDto(String title, String content, List<MultipartFile> files, String interests) {
        super(title, content, files);
        this.interests = interests;
    }

    @Override
    public GroupBoard toEntity(User user){
        return GroupBoard.boardBuilder()
                .title(title)
                .content(content)
                .interests(interests)
                .user(user)
                .build();
    }
}
