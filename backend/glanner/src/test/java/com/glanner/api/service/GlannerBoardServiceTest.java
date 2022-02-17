package com.glanner.api.service;

import com.glanner.api.dto.request.AddCommentReqDto;
import com.glanner.api.dto.request.SaveGlannerBoardReqDto;
import com.glanner.api.dto.response.FindGlannerBoardWithCommentsResDto;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.GlannerBoardRepository;
import com.glanner.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class GlannerBoardServiceTest {
    @Autowired
    private GlannerBoardService glannerBoardService;
    @Autowired
    private GroupBoardService groupBoardService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private GlannerService glannerService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GlannerBoardRepository boardRepository;

    private final String userEmail = "cherish8513@naver.com";

    @BeforeEach
    public void init(){
        createUser();
    }

    @Test
    void getGlannerBoards() {
        //given
        Long savedGlannerId = glannerService.saveGlanner(userEmail);

        SaveGlannerBoardReqDto glannerBoardReqDto = new SaveGlannerBoardReqDto("title", "content", new ArrayList<>(), savedGlannerId);
        Long glannerBoardId = glannerBoardService.saveGlannerBoard(userEmail, glannerBoardReqDto);

        AddCommentReqDto addCommentReqDto = new AddCommentReqDto(glannerBoardId, "content", null);
        boardService.addComment(userEmail, addCommentReqDto);

        //when
        List<FindGlannerBoardWithCommentsResDto> resDto = glannerBoardService.getGlannerBoards(savedGlannerId,0,10);

        //then
        assertThat(resDto.size()).isEqualTo(1);
        assertThat(resDto.get(0).getContent()).isEqualTo("content");
        assertThat(resDto.get(0).getComments().size()).isEqualTo(1);
        assertThat(resDto.get(0).getComments().get(0).getContent()).isEqualTo("content");

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