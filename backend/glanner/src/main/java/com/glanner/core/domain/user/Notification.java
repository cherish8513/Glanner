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
    public Notification(User user, String url, NotificationStatus confirmation, String content) {
        this.user = user;
        this.url = url;
        this.confirmation = confirmation;
        this.content = content;
    }

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String url;

    @Enumerated(EnumType.STRING)
    private NotificationStatus confirmation;
    private String content;
}
