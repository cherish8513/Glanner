package com.glanner.api.repository;

import com.glanner.api.dto.response.FindNotificationResDto;
import com.glanner.api.dto.response.FindWorkByTimeResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.NotificationQueryRepository;
import com.glanner.core.domain.glanner.DailyWorkGlanner;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.UserGlanner;
import com.glanner.core.domain.user.*;
import com.glanner.core.repository.DailyWorkScheduleRepository;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class NotificationQueryRepositoryImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationQueryRepository notificationQueryRepository;
    @Autowired
    private DailyWorkScheduleRepository dailyWorkScheduleRepository;
    @Autowired
    private DailyWorkScheduleRepository dailyWorkGlannerRepository;
    @Autowired
    private GlannerRepository glannerRepository;

    @BeforeEach
    public void init() {
        createUser();
    }

    @Test
    public void testFindAllByUserId() throws Exception{
        //given
        User user = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);
        createNotification("content1");
        createNotification("content2");
        createReadNotification("content3");

        //when
        List<FindNotificationResDto> findNotifications = notificationQueryRepository.findNotificationResDtoByUserId(user.getId());

        //then
        assertThat(findNotifications.size()).isEqualTo(3);
        assertThat(findNotifications.get(0).getTypeId()).isEqualTo(user.getNotifications().get(0).getTypeId());
        assertThat(findNotifications.get(0).getType()).isEqualTo(NotificationType.DAILY_WORK_SCHEDULE);
    }

    @Test
    public void testFindUnReadByUserId() throws Exception{
        //given
        User user = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);
        createNotification("content1");
        createNotification("content2");
        createReadNotification("content3");

        //when
        List<FindNotificationResDto> findNotifications = notificationQueryRepository.findUnreadNotificationResDtoByUserId(user.getId());

        //then
        assertThat(findNotifications.size()).isEqualTo(2);
        assertThat(findNotifications.get(0).getTypeId()).isEqualTo(user.getNotifications().get(0).getTypeId());
        assertThat(findNotifications.get(0).getType()).isEqualTo(NotificationType.DAILY_WORK_SCHEDULE);
    }

    @Test
    public void testFindValidScheduleWork() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();

        addWorks(now.plusMinutes(1), now.plusHours(1), now);            // 알림 O
        addWorks(now.plusMinutes(29), now.plusHours(1), now.minusMinutes(1)); // 알림 O
        addWorks(now.plusMinutes(40), now.plusHours(1), now.plusMinutes(10)); // 알림 X

        //when
        List<FindWorkByTimeResDto> findWorks = notificationQueryRepository.findScheduleWork();

        //then
        assertThat(dailyWorkScheduleRepository.count()).isEqualTo(3);
        assertThat(findWorks.size()).isEqualTo(2);
        assertThat(findWorks.get(0).getPhoneNumber()).isEqualTo("010-6575-2938");
        assertThat(findWorks.get(0).getTitle()).isEqualTo("work");
    }

    @Test
    public void testFindValidGlannerWork() throws Exception{
        //given
        Long savedGlannerId = createGlanner();
        LocalDateTime now = LocalDateTime.now();

        addGlannerWorks(savedGlannerId, now.plusMinutes(1), now.plusHours(1), now);            // 알림 O
        addGlannerWorks(savedGlannerId, now.plusMinutes(30), now.plusHours(1), now.plusMinutes(15)); // 알림 X
        addGlannerWorks(savedGlannerId, now.plusMinutes(40), now.plusHours(1), now.plusMinutes(10)); // 알림 X

        //when
        List<FindWorkByTimeResDto> findWorks = notificationQueryRepository.findGlannerWork();

        //then
        assertThat(findWorks.size()).isEqualTo(2);
        assertThat(findWorks.get(0).getTitle()).isEqualTo("work");
    }

    public void createUser(){
        User user = User.builder()
                .phoneNumber("010-6575-2938")
                .email("cherish8513@naver.com")
                .name("JeongJooHeon")
                .password("1234")
                .role(UserRoleStatus.ROLE_USER)
                .build();

        Schedule schedule = Schedule.builder()
                .build();
        user.changeSchedule(schedule);
        userRepository.save(user);
    }

    public void createNotification(String content){
        User user = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);
        Notification notification = Notification.builder()
                .user(user)
                .type(NotificationType.DAILY_WORK_SCHEDULE)
                .typeId(1L)
                .content(content)
                .confirmation(ConfirmStatus.STILL_NOT_CONFIRMED)
                .build();
        user.addNotification(notification);
    }
    public void createReadNotification(String content){
        User user = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);
        Notification notification = Notification.builder()
                .user(user)
                .type(NotificationType.DAILY_WORK_SCHEDULE)
                .typeId(1L)
                .content(content)
                .confirmation(ConfirmStatus.CONFIRM)
                .build();
        user.addNotification(notification);
    }
    private void addWorks(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime alarmDate) {
        User user = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);
        DailyWorkSchedule workSchedule = DailyWorkSchedule.builder()
                .content("hard")
                .title("work")
                .startDate(startDate)
                .endDate(endDate)
                .alarmDate(alarmDate)
                .build();
        user.getSchedule().addDailyWork(workSchedule);
    }

    public Long createGlanner(){
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(() -> new IllegalStateException("없는 회원 입니다."));

        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        //== 참여 유저 생성 ==//
        User anotherUser = User.builder()
                .email("cherish8514@naver.com")
                .phoneNumber("010-8797-3122")
                .role(UserRoleStatus.ROLE_USER)
                .build();

        Schedule schedule = Schedule.builder()
                .build();

        anotherUser.changeSchedule(schedule);

        User savedAnotherUser = userRepository.save(anotherUser);

        //== 글래너에 참여 유저 추가 ==//
        UserGlanner userGlanner1 = UserGlanner.builder()
                .user(savedAnotherUser)
                .build();

        glanner.addUserGlanner(userGlanner1);

        Glanner savedGlanner = glannerRepository.save(glanner);

        return savedGlanner.getId();
    }

    private void addGlannerWorks(Long savedGlannerId, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime alarmDate) {
        Glanner glanner = glannerRepository.findRealById(savedGlannerId).orElseThrow(IllegalArgumentException::new);
        glanner.addDailyWork(DailyWorkGlanner.builder()
                .content("hard")
                .title("work")
                .startDate(startDate)
                .endDate(endDate)
                .alarmDate(alarmDate)
                .build());
    }


}