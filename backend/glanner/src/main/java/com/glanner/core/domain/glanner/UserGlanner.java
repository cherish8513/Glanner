package com.glanner.core.domain.glanner;

import com.glanner.core.domain.base.BaseTimeEntity;
import com.glanner.core.domain.user.User;
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
public class UserGlanner extends BaseTimeEntity {

    @Builder
    public UserGlanner(User user) {
        this.user = user;
        user.addUserGlanner(this);
    }

    @Id @GeneratedValue
    @Column(name = "user_glanner_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "glanner_id")
    private Glanner glanner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void changeGlanner(Glanner glanner){
        this.glanner = glanner;
    }

    public void delete(){
        user.getUserGlanners().remove(this);
        glanner.getUserGlanners().remove(this);
    }
}
