package com.glanner.api.dto.request;

import com.glanner.core.domain.board.Board;
import com.glanner.core.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
public class SaveBoardReqDto {
    @NotNull
    public String title;
    @NotNull
    public String content;
    public List<MultipartFile> files;

    public SaveBoardReqDto(String title, String content, List<MultipartFile> files) {
        this.title = title;
        this.content = content;
        this.files = files;
    }

    public Board toEntity(User user) {
        return Board
                .builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
