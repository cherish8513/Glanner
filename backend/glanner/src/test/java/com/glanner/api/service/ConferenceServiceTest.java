package com.glanner.api.service;

import com.glanner.api.dto.request.AddParticipantReqDto;
import com.glanner.api.dto.request.OpenConferenceReqDto;
import com.glanner.api.dto.response.FindConferenceResDto;
import com.glanner.api.exception.ConferenceNotFoundException;
import com.glanner.api.exception.GlannerNotFoundException;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.UserGlanner;
import com.glanner.core.domain.user.*;
import com.glanner.core.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ConferenceServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GlannerRepository glannerRepository;
    @Autowired
    private ConferenceRepository conferenceRepository;
    @Autowired
    private UserConferenceRepository userConferenceRepository;
    @Autowired
    private UserConferenceCustomRepository userConferenceCustomRepository;

    private Glanner savedGlanner;

    @BeforeEach
    public void init(){
        createUser();
    }

    @Test
    void testOpenConference() {
        //given
        User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));
        OpenConferenceReqDto reqDto = new OpenConferenceReqDto(savedGlanner.getId(), 6);

        //when
        Glanner findGlanner = glannerRepository.findById(reqDto.getGlannerId()).orElseThrow(GlannerNotFoundException::new);

        Conference conference = reqDto.toEntity(findUser, findGlanner);

        Conference savedConference = conferenceRepository.save(conference);

        UserConference userConference = UserConference.builder()
                .user(findUser)
                .conference(savedConference)
                .build();

        UserConference savedUserConference = userConferenceRepository.save(userConference);

        //then
        assertThat(savedConference.getOwner()).isEqualTo(findUser);
        assertThat(savedConference.getGlanner()).isEqualTo(savedGlanner);
        assertThat(savedConference.getIsActive()).isEqualTo(ConferenceActiveStatus.ON);

        assertThat(savedUserConference.getConference()).isEqualTo(savedConference);
        assertThat(savedUserConference.getUser()).isEqualTo(findUser);
    }

    @Test
    void testCloseConference() {
        //given
        User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));

        Conference conference = Conference.builder()
                .owner(findUser)
                .glanner(savedGlanner)
                .attendLimit(5)
                .callStartTime(LocalDateTime.now())
                .callEndTime(null)
                .isActive(ConferenceActiveStatus.ON).build();

        Conference savedConference = conferenceRepository.save(conference);

        //when
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        Conference findConference = conferenceRepository.findById(savedConference.getId()).orElseThrow(ConferenceNotFoundException::new);
        findConference.changeEndTime(endTime);
        findConference.changeStatus();

        //then
        assertThat(savedConference.getCallEndTime()).isEqualTo(endTime);
        assertThat(savedConference.getIsActive()).isEqualTo(ConferenceActiveStatus.CLOSE);
    }

    @Test
    void testAddParticipant() {
        //given
        User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));
        Conference conference = Conference.builder()
                .owner(findUser)
                .glanner(savedGlanner)
                .attendLimit(5)
                .callStartTime(LocalDateTime.now())
                .callEndTime(null)
                .isActive(ConferenceActiveStatus.ON).build();

        Conference savedConference = conferenceRepository.save(conference);

        AddParticipantReqDto reqDto = new AddParticipantReqDto(savedConference.getId(), findUser.getId());

        //when
        User user = userRepository.findById(reqDto.getUserId()).orElseThrow(UserNotFoundException::new);
        Conference findConference = conferenceRepository.findById(reqDto.getConferenceId()).orElseThrow(ConferenceNotFoundException::new);
        UserConference addUserConference = UserConference.builder()
                .user(user)
                .conference(findConference)
                .build();
        UserConference savedUserConference = userConferenceRepository.save(addUserConference);

        //then
        assertThat(savedUserConference.getConference()).isEqualTo(savedConference);
        assertThat(savedUserConference.getUser()).isEqualTo(findUser);
        assertThat(userConferenceCustomRepository.findByConferenceId(findConference.getId()).size()).isEqualTo(1);
    }

    @Test
    void testFindConferenceDetail() {
        //given
        User findUser = getUser(userRepository.findByEmail("cherish8513@naver.com"));

        Conference conference = Conference.builder()
                .owner(findUser)
                .glanner(savedGlanner)
                .attendLimit(5)
                .callStartTime(LocalDateTime.now())
                .callEndTime(null)
                .isActive(ConferenceActiveStatus.ON).build();

        Conference savedConference = conferenceRepository.save(conference);

        UserConference userConference = UserConference.builder()
                .user(findUser)
                .conference(savedConference)
                .build();

        UserConference savedUserConference = userConferenceRepository.save(userConference);

        //when
        Conference findConference = conferenceRepository.findById(savedConference.getId()).orElseThrow(ConferenceNotFoundException::new);
        List<UserConference> userConferences = userConferenceCustomRepository.findByConferenceId(savedConference.getId());

        FindConferenceResDto findConferenceResDtos = new FindConferenceResDto(findConference, userConferences);

        //then
        assertThat(findConferenceResDtos.getConferenceId()).isEqualTo(findConference.getId());
        assertThat(findConferenceResDtos.getOwnerId()).isEqualTo(findUser.getId());
        assertThat(findConferenceResDtos.getAttendeeNumber()).isEqualTo(1);
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

        Glanner glanner = Glanner.builder()
                .host(user)
                .build();

        UserGlanner userGlanner = UserGlanner.builder()
                .user(user)
                .build();

        glanner.addUserGlanner(userGlanner);

        savedGlanner = glannerRepository.save(glanner);
    }

    public User getUser(Optional<User> user){
        return user.orElseThrow(UserNotFoundException::new);
    }

    public Glanner getGlanner(Optional<Glanner> glanner){
        return glanner.orElseThrow(() -> new IllegalArgumentException("글래너가 존재하지 않습니다."));
    }
}