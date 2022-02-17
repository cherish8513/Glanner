package com.glanner.api.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindPlannerWorkResDto {
    Long workId;
    String title;
    String content;
    LocalDateTime start;
    LocalDateTime end;
    LocalDateTime alarmDate;
}
