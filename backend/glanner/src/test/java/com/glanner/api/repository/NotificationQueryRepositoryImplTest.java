package com.glanner.api.repository;

import com.glanner.api.dto.response.FindWorkByTimeResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.NotificationQueryRepository;
import com.glanner.core.domain.glanner.DailyWorkGlanner;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.UserGlanner;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.DailyWorkScheduleRepository;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.UserRepository;
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

    @Test
    public void testFindValidScheduleWork() throws Exception{
        //given
        createUser();
        LocalDateTime now = LocalDateTime.now();

        addWorks(now.plusMinutes(1), now.plusHours(1), now);            // 알림 O
        addWorks(now.plusMinutes(29), now.plusHours(1), now.minusMinutes(1)); // 알림 O
        addWorks(now.plusMinutes(40), now.plusHours(1), now.plusMinutes(10)); // 알림 X

        //when
        List<FindWorkByTimeResDto> findWorks = notificationQueryRepository.findWorkBySchedule();

        //then
        assertThat(dailyWorkScheduleRepository.count()).isEqualTo(3);
        assertThat(findWorks.size()).isEqualTo(2);
        assertThat(findWorks.get(0).getPhoneNumber()).isEqualTo("010-6575-2938");
        assertThat(findWorks.get(0).getTitle()).isEqualTo("work");
    }

    @Test
    public void testFindValidGlannerWork() throws Exception{
        //given
        createUser();
        Long savedGlannerId = createGlanner();
        LocalDateTime now = LocalDateTime.now();

        addGlannerWorks(savedGlannerId, now.plusMinutes(1), now.plusHours(1), now);            // 알림 O
        addGlannerWorks(savedGlannerId, now.plusMinutes(30), now.plusHours(1), now.plusMinutes(15)); // 알림 X
        addGlannerWorks(savedGlannerId, now.plusMinutes(40), now.plusHours(1), now.plusMinutes(10)); // 알림 X

        //when
        List<FindWorkByTimeResDto> findWorks = notificationQueryRepository.findWorkByGlanner();

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

    private void addWorks(LocalDateTime start, LocalDateTime end, LocalDateTime noti) {
        User user = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);
        DailyWorkSchedule workSchedule = DailyWorkSchedule.builder()
                .content("hard")
                .title("work")
                .startDate(start)
                .endDate(end)
                .notiDate(noti)
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

    private void addGlannerWorks(Long savedGlannerId, LocalDateTime start, LocalDateTime end, LocalDateTime noti) {
        Glanner glanner = glannerRepository.findRealById(savedGlannerId).orElseThrow(IllegalArgumentException::new);
        glanner.addDailyWork(DailyWorkGlanner.builder()
                .content("hard")
                .title("work")
                .startDate(start)
                .endDate(end)
                .notiDate(noti)
                .build());
    }
}