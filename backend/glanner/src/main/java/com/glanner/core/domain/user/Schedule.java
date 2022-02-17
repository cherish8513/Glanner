package com.glanner.core.domain.user;

import com.glanner.core.domain.base.BaseTimeEntity;
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
public class Schedule extends BaseTimeEntity {

    @Builder
    public Schedule(User user) {
        this.user = user;
    }

    @Id @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    @OneToOne(mappedBy = "schedule", fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<DailyWorkSchedule> works = new ArrayList<>();

    public void changeUser(User user){
        this.user = user;
    }

    public void addDailyWork(DailyWorkSchedule work){
        works.add(work);
        work.changeSchedule(this);
    }

}
