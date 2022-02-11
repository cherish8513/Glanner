package com.glanner.api.service;

import com.glanner.api.dto.request.AddPlannerWorkReqDto;
import com.glanner.api.dto.request.SaveUserReqDto;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

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
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(UserNotFoundException::new);
        int workSize = findUser.getSchedule().getWorks().size();
        findUser.getSchedule().addDailyWork(reqDto.toEntity());

        //then
        assertThat(findUser.getSchedule().getWorks().size()).isEqualTo(workSize + 1);
        assertThat(findUser.getSchedule().getWorks().get(workSize).getTitle()).isEqualTo("title");
        assertThat(findUser.getSchedule().getWorks().get(workSize).getContent()).isEqualTo("content");
        assertThat(findUser.getSchedule().getWorks().get(workSize).getStartDate()).isEqualTo(now);

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

}
