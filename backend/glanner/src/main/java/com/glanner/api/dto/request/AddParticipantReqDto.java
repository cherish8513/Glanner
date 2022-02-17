package com.glanner.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddParticipantReqDto {
    @NotNull
    Long conferenceId;
    @NotNull
    Long userId;
}
