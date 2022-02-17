package com.glanner.core.domain.glanner;

import com.glanner.core.domain.board.Board;
import com.glanner.core.domain.user.User;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



@Entity
@QueryEntity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GlannerBoard extends Board {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "glanner_id")
    private Glanner glanner;

    @Builder(builderMethodName = "boardBuilder")
    public GlannerBoard(String title, String content, User user, Glanner glanner) {
        super(title, content, user);
        this.glanner = glanner;
    }

    public void changeGlanner(Glanner glanner){
        this.glanner = glanner;
    }
}
