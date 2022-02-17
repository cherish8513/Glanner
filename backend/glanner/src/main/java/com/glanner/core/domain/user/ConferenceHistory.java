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
public class ConferenceHistory extends BaseTimeEntity {

    @Builder
    public ConferenceHistory(Conference conference, User user, ConferenceHistoryStatus status) {
        this.conference = conference;
        this.user = user;
        this.status = status;
    }

    @Id @GeneratedValue
    @Column(name = "conference_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id")
    private Conference conference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ConferenceHistoryStatus status;
}
