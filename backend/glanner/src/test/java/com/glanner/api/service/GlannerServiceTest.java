package com.glanner.api.service;

import com.glanner.api.dto.request.*;
import com.glanner.api.dto.response.FindGlannerResDto;
import com.glanner.api.dto.response.SaveGroupBoardResDto;
import com.glanner.api.exception.AlreadyInGroupException;
import com.glanner.api.exception.BoardNotFoundException;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.core.domain.board.FileInfo;
import com.glanner.core.domain.glanner.DailyWorkGlanner;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GroupBoard;
import com.glanner.core.domain.glanner.UserGlanner;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private GroupBoardRepository groupBoardRepository;
    @Autowired
    private DailyWorkGlannerRepository dailyWorkGlannerRepository;
    @Autowired
    private UserGlannerRepository userGlannerRepository;
    @Autowired
    private GlannerService glannerService;
    @Autowired
    private GroupBoardService groupBoardService;
    @Autowired
    private UserService userService;

    private final String userEmail = "test@test.com";


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
        User findUser = getUser(userRepository.findByEmail(userEmail));


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
        LocalDateTime start = LocalDateTime.parse("2022-02-01 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        userService.saveUser(new SaveUserReqDto("name", "test2@test.com", "123456", "010-1234-5689"));
        SaveGroupBoardResDto saveGroupBoardResDto = groupBoardService.saveGroupBoard(userEmail, new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "interest"));
        Long glannerId = saveGroupBoardResDto.getGlannerId();
        glannerService.addDailyWork(new AddGlannerWorkReqDto(glannerId, "title", "content", start, start.plusHours(3), null));
        glannerService.addDailyWork(new AddGlannerWorkReqDto(glannerId, "title", "content", start.plusHours(4), start.plusHours(8), null));
        glannerService.addUser(new AddUserToGlannerReqDto(glannerId, "test2@test.com"));

        //when
        glannerService.deleteGlanner(glannerId);

        //then
        assertThatThrownBy(() -> {
            groupBoardRepository.findRealById(saveGroupBoardResDto.getGroupBoardId()).orElseThrow(BoardNotFoundException::new);
        }).isInstanceOf(BoardNotFoundException.class);

        assertThatThrownBy(() -> {
            getGlanner(glannerRepository.findRealById(glannerId));
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("글래너가 존재하지 않습니다.");
    }

    @Test
    public void testAddUser() throws Exception{
        //given
        User findUser = getUser(userRepository.findByEmail(userEmail));
        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        Glanner savedGlanner = glannerRepository.save(glanner);

        User anotherUser = User.builder()
                .email("test@naver.com")
                .role(UserRoleStatus.ROLE_USER)
                .build();

        Schedule schedule = Schedule.builder()
                .build();
        anotherUser.changeSchedule(schedule);
        userRepository.save(anotherUser);

        int size = 5;
        AddUserToGlannerReqDto reqDto = new AddUserToGlannerReqDto(savedGlanner.getId(),"test@naver.com");

        //when
        User findAnotherUser = getUser(userRepository.findByEmail(reqDto.getEmail()));
        Glanner findGlanner = getGlanner(glannerRepository.findRealById(savedGlanner.getId()));

        if(userGlannerRepository.findByUserIdAndGlannerId(findAnotherUser.getId(), findGlanner.getId()) != null){
            throw new AlreadyInGroupException();
        }

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
        Long glannerId = glannerService.saveGlanner(userEmail);

        //== 참여 유저 생성 ==//
        Long attendedId = userService.saveUser(new SaveUserReqDto("name", "test2@test.com", "123456", "010-1234-5689"));

        //== 글래너에 참여 유저 추가 ==//
        glannerService.addUser(new AddUserToGlannerReqDto(glannerId, "test2@test.com"));

        //when
        //== 글래너에 호스트와 유저 1명이 있는 상황 ==//
        //== 유저 1명이 가지고 있는 글래너는 1개 ==//
        glannerService.deleteUser(glannerId, attendedId);

        //then
        User deleteAfterUser = getUser(userRepository.findByEmail("test2@test.com"));
        assertThat(deleteAfterUser.getUserGlanners().size()).isEqualTo(0);
        Glanner deleteAfterGlanner = getGlanner(glannerRepository.findById(glannerId));
        assertThat(deleteAfterGlanner.getUserGlanners().size()).isEqualTo(1);
        System.out.println(deleteAfterGlanner.getUserGlanners().get(0).getUser().getEmail());
    }

    @Test
    public void deleteDailyWork() throws Exception{
        //given
        User findUser = getUser(userRepository.findByEmail(userEmail));
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
        User findUser = getUser(userRepository.findByEmail(userEmail));
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
        UpdateGlannerWorkReqDto reqDto = new UpdateGlannerWorkReqDto(workId, "title", null, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        //when
        DailyWorkGlanner updateWork = dailyWorkGlannerRepository.findById(reqDto.getWorkId()).orElseThrow(IllegalArgumentException::new);
        updateWork.changeDailyWork(reqDto.getStartDate(), reqDto.getEndDate(), reqDto.getAlarmDate(), reqDto.getTitle(), reqDto.getContent());

        //then
        assertThat(updateWork.getTitle()).isEqualTo("title");
    }

    @Test
    public void testFindAGlannerInfo() throws Exception{
        //given
        User findUser = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalStateException("없는 회원 입니다."));

        SaveGroupBoardReqDto reqDto = new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "null");
        GroupBoard groupBoard = reqDto.toEntity(findUser);

        Glanner glanner = Glanner.builder()
                .host(findUser)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(findUser)
                .build();

        glanner.addUserGlanner(userGlanner);

        Glanner savedGlanner = glannerRepository.save(glanner);
        groupBoard.changeGlanner(glanner);
        groupBoardRepository.save(groupBoard);

        Long savedGlannerId = savedGlanner.getId();

        //when
        Glanner findGlanner = glannerRepository.findRealById(savedGlannerId).orElseThrow(IllegalArgumentException::new);
        GroupBoard findGroupBoard = groupBoardRepository.findByGlannerId(findGlanner.getId()).orElseThrow(BoardNotFoundException::new);
        List<UserGlanner> findUserGlanners = findGlanner.getUserGlanners();

        FindGlannerResDto findGlannerResDto = new FindGlannerResDto(findGlanner, findGroupBoard, findUserGlanners);

        //then
        assertThat(findGlannerResDto.getGlannerId()).isEqualTo(savedGlannerId);
        assertThat(findGlannerResDto.getHostEmail()).isEqualTo(findGlanner.getHost().getEmail());
        assertThat(findGlannerResDto.getNumOfMember()).isEqualTo(1);
        assertThat(findGlannerResDto.getMembersInfos().size()).isEqualTo(1);
        assertThat(findGlannerResDto.getMembersInfos().get(0).getUserId()).isEqualTo(findUser.getId());
        assertThat(findGlannerResDto.getMembersInfos().get(0).getUserEmail()).isEqualTo(userEmail);
        assertThat(findGlannerResDto.getMembersInfos().get(0).getUserName()).isEqualTo("JeongJooHeon");
        assertThat(findGlannerResDto.getGroupBoardId()).isEqualTo(findGroupBoard.getId());
    }

    public void createUser(){
        User user = User.builder()
                .phoneNumber("010-6575-2938")
                .email(userEmail)
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
