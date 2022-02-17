package com.glanner.api.dto.request;

import com.glanner.core.domain.board.NoticeBoard;
import com.glanner.core.domain.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class SaveNoticeBoardReqDto extends SaveBoardReqDto{
    public int likeCount;
    public int dislikeCount;

    public SaveNoticeBoardReqDto(String title, String content, List<MultipartFile> files) {
        super(title, content, files);
    }

    @Override
    public NoticeBoard toEntity(User user){
        return NoticeBoard.boardBuilder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
