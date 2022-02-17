package com.glanner.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.glanner.core.domain.user.DailyWorkSchedule;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddPlannerWorkReqDto {
    @NotNull
    String title;
    String content;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime startDate;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime alarmDate;


    public DailyWorkSchedule toEntity(){
        return DailyWorkSchedule
                .builder()
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .alarmDate(alarmDate)
                .build();
    }
}