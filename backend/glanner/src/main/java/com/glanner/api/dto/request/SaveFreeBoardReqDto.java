package com.glanner.api.dto.request;

import com.glanner.core.domain.board.FreeBoard;
import com.glanner.core.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@NoArgsConstructor
@Getter
public class SaveFreeBoardReqDto extends SaveBoardReqDto {
    public SaveFreeBoardReqDto(String title, String content, List<MultipartFile> files) {
        super(title, content, files);
    }

    @Override
    public FreeBoard toEntity(User user){
        return FreeBoard.boardBuilder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
