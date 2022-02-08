package com.glanner.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SaveGlannerBoardReqDto extends SaveBoardReqDto{
    @NotNull
    public Long glannerId;

    public SaveGlannerBoardReqDto(String title, String content, List<MultipartFile> files, Long glannerId) {
        super(title, content, files);
        this.glannerId = glannerId;
    }
}
