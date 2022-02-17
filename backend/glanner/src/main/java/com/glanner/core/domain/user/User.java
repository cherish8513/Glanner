package com.glanner.core.domain.user;

import com.glanner.core.domain.base.BaseTimeEntity;
import com.glanner.core.domain.glanner.UserGlanner;
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
public class User extends BaseTimeEntity {

    @Builder
    public User(String name, String email, String password, String phoneNumber, UserRoleStatus role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRoleStatus role;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    List<UserGlanner> userGlanners = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    List<Notification> notifications = new ArrayList<>();


    public void changeSchedule(Schedule schedule){
        this.schedule = schedule;
        schedule.changeUser(this);
    }

    public void changePassword(String password){
        this.password = password;
    }
    public void addUserGlanner(UserGlanner userGlanner){
        this.userGlanners.add(userGlanner);
    }
    public void addNotification(Notification notification){
        this.notifications.add(notification);
    }
}
