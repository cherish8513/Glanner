package com.glanner.core.domain.glanner;

import com.glanner.core.domain.board.Board;
import com.glanner.core.domain.user.User;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@QueryEntity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupBoard extends Board {

    private String interests;

    @OneToOne
    @JoinColumn(name = "glanner_id")
    Glanner glanner;

    @Builder(builderMethodName = "boardBuilder")
    public GroupBoard(User user, String title, String content, String interests) {
        super(title, content, user);
        this.interests = interests;
    }

    public void changeGroupBoard(String title, String content, String interests, String fileUrls){
        this.interests = interests;
    }

    public void changeInterests(String interests){
        this.interests = interests;
    }

    public void changeGlanner(Glanner glanner){
        this.glanner = glanner;
    }
}
