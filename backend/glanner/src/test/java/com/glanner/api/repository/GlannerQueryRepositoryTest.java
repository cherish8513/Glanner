package com.glanner.api.repository;

import com.glanner.api.dto.response.FindAttendedGlannerResDto;
import com.glanner.api.dto.response.FindGlannerWorkResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.GlannerQueryRepository;
import com.glanner.core.domain.glanner.DailyWorkGlanner;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.UserGlanner;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class GlannerQueryRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GlannerRepository glannerRepository;

    @Autowired
    private GlannerQueryRepository glannerQueryRepository;

    private Long savedGlannerId;

    @BeforeEach
    public void init() {
        createUser();
        savedGlannerId = createGlanner();
        createGlanner();
        createGlanner();
        createGlanner();
        createGlanner();
    }

    @Test
    public void testFindUserAttendGlanner() throws Exception{
        //given
        User user = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);

        //when
        List<FindAttendedGlannerResDto> userAttended = glannerQueryRepository.findAttendedGlannersDtoByUserId(user.getId());

        //then
        assertThat(userAttended.size()).isEqualTo(5);
    }

    @Test
    public void testFindGlannerDailyWorks() throws Exception{
        //given
        Glanner glanner = glannerRepository.findRealById(savedGlannerId).orElseThrow(IllegalArgumentException::new);
        String format = "2022-02-01";
        LocalDateTime dateTimeStart = LocalDate.parse(format, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        LocalDateTime dateTimeEnd = dateTimeStart.plusMonths(1);
        for (int i = 0; i < 30; i++) {
            glanner.addDailyWork(DailyWorkGlanner
                    .builder()
                    .title("title")
                    .content("content")
                    .startDate(dateTimeStart.plusDays(i))
                    .endDate(dateTimeStart.plusDays(i).plusHours(i + 3))
                    .build());
        }

        //when
        List<FindGlannerWorkResDto> responseDto = glannerQueryRepository.findDailyWorksDtoWithPeriod(savedGlannerId, dateTimeStart, dateTimeEnd);

        //then
        for (FindGlannerWorkResDto findGlannerWorkResDto : responseDto) {
            assertThat(findGlannerWorkResDto.getStart().getMonthValue()).isEqualTo(2);
        }
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

        DailyWorkSchedule workSchedule = DailyWorkSchedule.builder()
                .content("hard")
                .title("work")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusHours(3))
                .build();
        schedule.addDailyWork(workSchedule);
        user.changeSchedule(schedule);
        userRepository.save(user);
    }

    private Long createGlanner() {
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(() -> new IllegalStateException("없는 회원 입니다."));

        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        Glanner savedGlanner = glannerRepository.save(glanner);
        return savedGlanner.getId();
    }
}
