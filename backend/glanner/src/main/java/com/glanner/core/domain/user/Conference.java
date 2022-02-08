package com.glanner.core.domain.user;

import com.glanner.core.domain.base.BaseTimeEntity;
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
    public Conference(User owner, LocalDateTime callStartTime, LocalDateTime callEndTime, String thumbnailUrl, String title, String description, ConferenceActiveStatus isActive) {
        this.owner = owner;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.description = description;
        this.isActive = isActive;
    }

    @Id @GeneratedValue
    @Column(name = "conference_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    private LocalDateTime callStartTime;
    private LocalDateTime callEndTime;
    private String thumbnailUrl;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private ConferenceActiveStatus isActive;
}
