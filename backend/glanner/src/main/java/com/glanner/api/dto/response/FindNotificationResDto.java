package com.glanner.api.dto.response;

import com.glanner.core.domain.user.ConfirmStatus;
import com.glanner.core.domain.user.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FindNotificationResDto {
    NotificationType type;
    Long typeId;
    ConfirmStatus confirmation;
    String content;
    LocalDateTime createdDate;
}
