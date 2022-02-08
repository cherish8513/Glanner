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
public class UserConference extends BaseTimeEntity {

    @Builder
    public UserConference(Conference conference, User user) {
        this.conference = conference;
        this.user = user;
    }

    @Id @GeneratedValue
    @Column(name = "user_conference_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id")
    private Conference conference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
