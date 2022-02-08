package com.glanner.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime startTime;

    @NotNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH-mm")
    LocalDateTime endTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH-mm")
    LocalDateTime notiTime;


    public DailyWorkSchedule toEntity(){
        return DailyWorkSchedule
                .builder()
                .title(title)
                .content(content)
                .startDate(startTime)
                .endDate(endTime)
                .notiDate(notiTime)
                .build();
    }
}