package com.glanner.core.domain.user;

import com.glanner.core.domain.base.BaseTimeEntity;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@QueryEntity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {

    @Builder
    public Notification(User user, NotificationType type, Long typeId, ConfirmStatus confirmation, String content) {
        this.user = user;
        this.type = type;
        this.typeId = typeId;
        this.confirmation = confirmation;
        this.content = content;
    }

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationType type;
    private Long typeId;

    @Enumerated(EnumType.STRING)
    private ConfirmStatus confirmation;
    private String content;

    public void changeStatus(){
        this.confirmation = ConfirmStatus.CONFIRM;
    }
}
