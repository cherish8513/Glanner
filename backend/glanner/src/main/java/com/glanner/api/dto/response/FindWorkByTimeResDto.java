package com.glanner.api.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindWorkByTimeResDto {
    Long dailyWorkId;
    String title;
    Long userId;
    String phoneNumber;
}
