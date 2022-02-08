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
public class Log extends BaseTimeEntity {

    @Builder
    public Log(User user, String content, String url) {
        this.user = user;
        this.content = content;
        this.url = url;
    }

    @Id @GeneratedValue
    @Column(name = "log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private String url;

    public void changeUser(User user){
        this.user = user;
    }
}
