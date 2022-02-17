package com.glanner.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindAttendedGlannerResDto {
    Long glannerId;
    String glannerName;
    String hostEmail;
}
