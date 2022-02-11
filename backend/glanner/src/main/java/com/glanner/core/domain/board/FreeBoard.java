package com.glanner.core.domain.board;

import com.glanner.core.domain.user.User;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@QueryEntity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FreeBoard extends Board {

    private int likeCount;
    private int dislikeCount;

    @Builder(builderMethodName = "boardBuilder")
    public FreeBoard(String title, String content, User user) {
        super(title, content, user);
        this.likeCount = 0;
        this.dislikeCount = 0;
    }

    public void addLike(){
        this.likeCount++;
    }

    public void addDislike(){
        this.dislikeCount++;
    }
}
