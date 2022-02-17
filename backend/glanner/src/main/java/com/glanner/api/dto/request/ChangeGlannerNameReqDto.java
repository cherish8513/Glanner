package com.glanner.api.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangeGlannerNameReqDto {
    @NotNull
    Long glannerId;
    @NotNull
    String glannerName;
}
