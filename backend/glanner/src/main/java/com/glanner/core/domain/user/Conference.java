package com.glanner.core.domain.user;

import com.glanner.core.domain.base.BaseTimeEntity;
import com.glanner.core.domain.glanner.Glanner;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@QueryEntity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Conference extends BaseTimeEntity {

    @Builder
    public Conference(User owner, Glanner glanner, LocalDateTime callStartTime, LocalDateTime callEndTime, int attendLimit, ConferenceActiveStatus isActive) {
        this.owner = owner;
        this.glanner = glanner;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
        this.attendLimit = attendLimit;
        this.isActive = isActive;
    }

    @Id @GeneratedValue
    @Column(name = "conference_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "glanner_id")
    private Glanner glanner;

    private LocalDateTime callStartTime;
    private LocalDateTime callEndTime;

    private int attendLimit;

    @Enumerated(EnumType.STRING)
    private ConferenceActiveStatus isActive;

    public void changeStatus() {
        this.isActive = ConferenceActiveStatus.CLOSE;
    }
    public void changeEndTime(LocalDateTime now) {
        this.callEndTime = now;
    }
}
