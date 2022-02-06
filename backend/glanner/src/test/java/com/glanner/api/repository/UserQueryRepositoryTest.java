package com.glanner.api.repository;

import com.glanner.api.dto.response.FindPlannerWorkResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.UserQueryRepository;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserQueryRepositoryTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserQueryRepository userQueryRepository;

    @BeforeEach
    public void init(){
        createUser();
        for (int i = 0; i < 20; i++) {
        addWorks(LocalDateTime.now().plusDays(i), LocalDateTime.now().plusDays(i).plusHours(3 + i));
        }
    }


    @Test
    public void testFindThisMonthPlan() throws Exception{
        //given
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        format += "-01";
        LocalDateTime month = LocalDate.parse(format, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);

        //when
        List<FindPlannerWorkResDto> dailyWorks = userQueryRepository.findDailyWorks(findUser.getSchedule().getId(), month);

        //then
        assertThat(dailyWorks.size()).isEqualTo(20);
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
        em.flush();
        em.clear();
    }

    private void addWorks(LocalDateTime now, LocalDateTime plusHours) {
        User user = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);
        DailyWorkSchedule workSchedule = DailyWorkSchedule.builder()
                .content("hard")
                .title("work")
                .startDate(now)
                .endDate(plusHours)
                .build();
        user.getSchedule().addDailyWork(workSchedule);
    }
}
