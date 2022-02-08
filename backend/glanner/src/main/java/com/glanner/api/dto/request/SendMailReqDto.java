package com.glanner.api.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SendMailReqDto {
    @NotNull
    public String address;
    @NotNull
    public String title;
    @NotNull
    public String content;
}
