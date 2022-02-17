package com.glanner.core.domain.glanner;

import com.glanner.core.domain.base.BaseTimeEntity;
import com.glanner.core.domain.user.User;
import com.querydsl.core.annotations.QueryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@QueryEntity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Glanner extends BaseTimeEntity {

    @Builder
    public Glanner(User host, String name) {
        this.host = host;
        this.name = name;
    }

    @Id @GeneratedValue
    @Column(name = "glanner_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User host;

    @OneToMany(mappedBy = "glanner", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<UserGlanner> userGlanners = new ArrayList<>();

    @OneToMany(mappedBy = "glanner", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<DailyWorkGlanner> works = new ArrayList<>();

    @OneToMany(mappedBy = "glanner", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<GlannerBoard> glannerBoards = new ArrayList<>();

    public void addUserGlanner(UserGlanner userGlanner){
        userGlanners.add(userGlanner);
        userGlanner.changeGlanner(this);
    }

    public void addDailyWork(DailyWorkGlanner work){
        works.add(work);
        work.changeGlanner(this);
    }

    public void addGlannerBoard(GlannerBoard glannerBoard){
        glannerBoards.add(glannerBoard);
        glannerBoard.changeGlanner(this);
    }

    public void changeGlannerName(String name){
        this.name = name;
    }
}

