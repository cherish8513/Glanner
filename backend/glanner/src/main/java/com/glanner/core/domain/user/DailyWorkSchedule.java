package com.glanner.core.domain.user;

import com.glanner.core.domain.base.BaseEntity;
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
public class DailyWorkSchedule extends BaseEntity {

    @Builder
    public DailyWorkSchedule(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime alarmDate, String title, String content) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        changeAlarmDate(alarmDate);
    }

    @Id @GeneratedValue
    @Column(name = "daily_work_schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String title;
    private String content;

    public void changeSchedule(Schedule schedule){
        this.schedule = schedule;
    }

    public void changeDailyWork(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime alarmDate, String title, String content){
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        changeAlarmDate(alarmDate);
    }

    public void cancel(){
        schedule.getWorks().remove(this);
    }
}
