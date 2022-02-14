package com.glanner.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.glanner.core.domain.glanner.DailyWorkGlanner;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddGlannerWorkReqDto {
    @NotNull
    Long glannerId;
    @NotNull
    String title;
    String content;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime startTime;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime endTime;

    @ApiModelProperty(value = "일정 시작 시간 - 타이머 설정 시간")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime notiTime;

    public DailyWorkGlanner toEntity(){
        return DailyWorkGlanner
                .builder()
                .title(title)
                .content(content)
                .startDate(startTime)
                .endDate(endTime)
                .notiDate(notiTime)
                .build();
    }
}
