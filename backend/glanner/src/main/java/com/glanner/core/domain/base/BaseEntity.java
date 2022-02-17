package com.glanner.core.domain.base;

import com.glanner.core.domain.user.ConfirmStatus;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity{
    @Enumerated(EnumType.STRING)
    private ConfirmStatus confirmStatus = ConfirmStatus.STILL_NOT_CONFIRMED;

    @LastModifiedBy
    private String modifiedBy;

    private LocalDateTime alarmDate;

    public void confirm(){
        this.confirmStatus = ConfirmStatus.CONFIRM;
    }

    public void changeAlarmDate(LocalDateTime alarmDate){
        this.alarmDate = alarmDate;
    }
}
