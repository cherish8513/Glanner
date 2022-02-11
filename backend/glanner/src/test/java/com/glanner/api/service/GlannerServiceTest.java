package com.glanner.api.service;

import com.glanner.api.dto.request.AddUserToGlannerReqDto;
import com.glanner.api.dto.request.UpdateGlannerWorkReqDto;
import com.glanner.api.dto.response.FindGlannerResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.core.domain.glanner.DailyWorkGlanner;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.UserGlanner;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.DailyWorkGlannerRepository;
import com.glanner.core.repository.GlannerRepository;
import com.glanner.core.repository.UserGlannerRepository;
import com.glanner.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class GlannerServiceTest {

    @Autowired
    private GlannerRepository glannerRepository;
    @Autowired
    private DailyWorkGlannerRepository dailyWorkGlannerRepository;
    @Autowired
    private UserGlannerRepository userGlannerRepository;


    // save user
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init(){
        createUser();
    }

    @Test
    public void testCreateGlanner() throws Exception{
        //given
       User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));


        //when
        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        Glanner savedGlanner = glannerRepository.save(glanner);

        //then
        assertThat(savedGlanner.getHost()).isEqualTo(findUser);
        assertThat(savedGlanner.getUserGlanners().get(0).getUser()).isEqualTo(findUser);
    }

    @Test
    public void testDeleteGlanner() throws Exception{
        //given
        User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));
        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        Glanner savedGlanner = glannerRepository.save(glanner);

        //when
        Glanner findGlanner = getGlanner(glannerRepository.findRealById(savedGlanner.getId()));
        glannerRepository.deleteAllWorksById(findGlanner.getId());
        glannerRepository.deleteAllUserGlannerById(findGlanner.getId());
        glannerRepository.delete(findGlanner);

        //then
        assertThatThrownBy(() -> {
            getGlanner(glannerRepository.findRealById(savedGlanner.getId()));
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("글래너가 존재하지 않습니다.");
    }

    @Test
    public void testAddUser() throws Exception{
        //given
        User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));
        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        Glanner savedGlanner = glannerRepository.save(glanner);

        User anotherUser = User.builder()
                .email("cherish8514@naver.com")
                .role(UserRoleStatus.ROLE_USER)
                .build();

        Schedule schedule = Schedule.builder()
                .build();
        anotherUser.changeSchedule(schedule);
        userRepository.save(anotherUser);

        int size = 5;
        AddUserToGlannerReqDto reqDto = new AddUserToGlannerReqDto(savedGlanner.getId(),"cherish8514@naver.com");

        //when
        User findAnotherUser = getUser(userRepository.findByEmail(reqDto.getEmail()));
        Glanner findGlanner = getGlanner(glannerRepository.findRealById(savedGlanner.getId()));
        if (findGlanner.getUserGlanners().size() >= size){
            throw new IllegalStateException("회원 수가 가득 찼습니다.");
        }
        UserGlanner userGlanner1 = UserGlanner.builder()
                .user(findAnotherUser)
                .build();
        findGlanner.addUserGlanner(userGlanner1);

        //then
        assertThat(findAnotherUser.getUserGlanners().get(0).getGlanner()).isEqualTo(findGlanner);
        assertThat(findGlanner.getUserGlanners().size()).isEqualTo(2);
    }

    @Test
    public void testDeleteUser() throws Exception{
        //given
        //== glanner 생성 ==//
        User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));
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

        //when
        //== 글래너에 호스트와 유저 1명이 있는 상황 ==//
        //== 유저 1명이 가지고 있는 글래너는 1개 ==//
        User attendingUser = getUser(userRepository.findByEmail("cherish8514@naver.com"));
        Glanner findGlanner = getGlanner(glannerRepository.findRealById(savedGlanner.getId()));
        int size = findGlanner.getUserGlanners().size();
        for (int i = 0; i < size; i++) {
            if(findGlanner.getUserGlanners().get(i).getUser().getId().equals(attendingUser.getId())){
                findGlanner.getUserGlanners().get(i).delete();
                break;
            }
        }

        //then
        User deleteAfterUser = getUser(userRepository.findByEmail("cherish8514@naver.com"));
        assertThat(deleteAfterUser.getUserGlanners().size()).isEqualTo(0);
    }

    @Test
    public void deleteDailyWork() throws Exception{
        //given
        User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));
        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);
        glanner.addDailyWork(DailyWorkGlanner.builder().build());

        Glanner savedGlanner = glannerRepository.save(glanner);
        Long workId = savedGlanner.getWorks().get(0).getId();

        //when
        Glanner findGlanner = getGlanner(glannerRepository.findRealById(savedGlanner.getId()));
        DailyWorkGlanner deleteWork = dailyWorkGlannerRepository.findById(workId).orElseThrow(IllegalArgumentException::new);
        findGlanner.getWorks().remove(deleteWork);

        //then
        assertThat(findGlanner.getWorks().size()).isEqualTo(0);
    }

    @Test
    public void updateDailyWork() throws Exception{
        //given
        User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));
        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);
        glanner.addDailyWork(DailyWorkGlanner.builder().build());

        Glanner savedGlanner = glannerRepository.save(glanner);
        Long workId = savedGlanner.getWorks().get(0).getId();
        UpdateGlannerWorkReqDto reqDto = new UpdateGlannerWorkReqDto(workId, "title", null, LocalDateTime.now(), LocalDateTime.now());

        //when
        DailyWorkGlanner updateWork = dailyWorkGlannerRepository.findById(reqDto.getWorkId()).orElseThrow(IllegalArgumentException::new);
        updateWork.changeDailyWork(reqDto.getStartTime(), reqDto.getEndTime(), reqDto.getTitle(), reqDto.getContent());

        //then
        assertThat(updateWork.getTitle()).isEqualTo("title");
    }

    @Test
    public void testFindAGlannerInfo() throws Exception{
        //given
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(() -> new IllegalStateException("없는 회원 입니다."));

        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        Glanner savedGlanner = glannerRepository.save(glanner);
        Long savedGlannerId = savedGlanner.getId();

        //when
        Glanner findGlanner = glannerRepository.findRealById(savedGlannerId).orElseThrow(IllegalArgumentException::new);
        List<UserGlanner> findUserGlanners = userGlannerRepository.findByGlannerId(savedGlannerId);
        FindGlannerResDto findGlannerResDto = new FindGlannerResDto(findGlanner, findUserGlanners);

        //then
        assertThat(findGlannerResDto.getGlannerId()).isEqualTo(savedGlannerId);
        assertThat(findGlannerResDto.getHostEmail()).isEqualTo(findGlanner.getHost().getEmail());
        assertThat(findGlannerResDto.getNumOfMember()).isEqualTo(1);
        assertThat(findGlannerResDto.getMembersEmail().get(0)).isEqualTo("cherish8513@naver.com");
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

    public User getUser(Optional<User> user){
        return user.orElseThrow(UserNotFoundException::new);
    }

    public Glanner getGlanner(Optional<Glanner> glanner){
        return glanner.orElseThrow(() -> new IllegalArgumentException("글래너가 존재하지 않습니다."));
    }
}
