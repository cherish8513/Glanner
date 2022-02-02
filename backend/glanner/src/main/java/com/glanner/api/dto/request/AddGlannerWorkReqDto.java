package com.glanner.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.glanner.core.domain.glanner.DailyWorkGlanner;
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
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss")
    LocalDateTime startTime;

    @NotNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss")
    LocalDateTime endTime;

    public DailyWorkGlanner toEntity(){
        return DailyWorkGlanner
                .builder()
                .title(title)
                .content(content)
                .startDate(startTime)
                .endDate(endTime)
                .build();
    }
}
