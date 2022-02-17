package com.glanner.api.dto.request;


import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.user.Conference;
import com.glanner.core.domain.user.ConferenceActiveStatus;
import com.glanner.core.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OpenConferenceReqDto {
    @NotNull
    Long glannerId;
    @NotNull
    int attendLimit;

    public Conference toEntity(User user, Glanner glanner){
        return Conference.builder()
                .owner(user)
                .glanner(glanner)
                .callStartTime(LocalDateTime.now())
                .callEndTime(null)
                .attendLimit(attendLimit)
                .isActive(ConferenceActiveStatus.ON)
                .build();
    }
}
