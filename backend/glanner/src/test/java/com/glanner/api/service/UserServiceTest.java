package com.glanner.api.service;

import com.glanner.api.dto.request.AddPlannerWorkReqDto;
import com.glanner.api.dto.request.SaveUserReqDto;
import com.glanner.api.dto.response.FindPlannerWorkResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private final String userEmail = "cherish8513@naver.com";

    @BeforeEach
    public void init(){
        createUser();
    }

    @Test
    public void testJoin() throws Exception{
        //given
        SaveUserReqDto reqDto = SaveUserReqDto.builder()
                .email("cherish8515@naver.com")
                .name("싸피")
                .password("1234")
                .phoneNumber("010-1234-5678")
                .build();

        //when
        User user = reqDto.toEntity();
        user.changePassword(passwordEncoder.encode(user.getPassword()));

        validateDuplicateMember(user);

        Schedule schedule = Schedule.builder()
                .build();

        user.changeSchedule(schedule);
        User savedUser = userRepository.save(user);

        //then
        assertThat(savedUser.getSchedule()).isEqualTo(schedule);
    }

    @Test
    public void testAddWork() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        AddPlannerWorkReqDto reqDto = new AddPlannerWorkReqDto("title", "content", now, now.plusDays(3), null);

        //when
        userService.addWork(userEmail, reqDto);

        //then
        List<DailyWorkSchedule> works = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new).getSchedule().getWorks();
        assertThat(works.size()).isEqualTo(1);
        assertThat(works.get(0).getContent()).isEqualTo("content");
    }

    @Test
    public void testGetWork() throws Exception{
        //given
        LocalDateTime start = LocalDateTime.parse("2022-02-01 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime end = start.plusMonths(1);
        AddPlannerWorkReqDto reqDto1 = new AddPlannerWorkReqDto("title", "content", start, start.plusDays(3), null);
        AddPlannerWorkReqDto reqDto2 = new AddPlannerWorkReqDto("title", "first", start.plusDays(1), start.plusDays(4), null);
        AddPlannerWorkReqDto reqDto3 = new AddPlannerWorkReqDto("title", "second", start.plusDays(2), start.plusDays(5), null);
        userService.addWork(userEmail, reqDto1);
        userService.addWork(userEmail, reqDto2);
        userService.addWork(userEmail, reqDto3);

        //when
        List<FindPlannerWorkResDto> works = userService.getWorks(userEmail, start.plusHours(1), end);

        //then
        assertThat(works.size()).isEqualTo(2);
        assertThat(works.get(0).getContent()).isEqualTo("first");
        assertThat(works.get(1).getContent()).isEqualTo("second");
    }

    private void validateDuplicateMember(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m -> {throw new IllegalStateException("이미 존재하는 회원입니다");
                });
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

}
