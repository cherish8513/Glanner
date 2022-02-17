package com.glanner.api.repository;

import com.glanner.api.dto.request.*;
import com.glanner.api.dto.response.FindFreeBoardResDto;
import com.glanner.api.dto.response.FindGlannerBoardResDto;
import com.glanner.api.dto.response.FindGroupBoardResDto;
import com.glanner.api.dto.response.FindNoticeBoardResDto;
import com.glanner.api.queryrepository.FreeBoardQueryRepository;
import com.glanner.api.queryrepository.GlannerBoardQueryRepository;
import com.glanner.api.queryrepository.GroupBoardQueryRepository;
import com.glanner.api.queryrepository.NoticeBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.GlannerBoardService;
import com.glanner.api.service.GlannerService;
import com.glanner.api.service.GroupBoardService;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.FreeBoardRepository;
import com.glanner.core.repository.GroupBoardRepository;
import com.glanner.core.repository.NoticeBoardRepository;
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

@Transactional
@SpringBootTest
public class BoardQueryRepositoryTest {
    @Autowired
    FreeBoardRepository freeBoardRepository;
    @Autowired
    NoticeBoardRepository noticeBoardRepository;
    @Autowired
    GroupBoardRepository groupBoardRepository;
    @Autowired
    FreeBoardQueryRepository freeBoardQueryRepository;
    @Autowired
    GlannerBoardQueryRepository glannerBoardQueryRepository;
    @Autowired
    GroupBoardQueryRepository groupBoardQueryRepository;
    @Autowired
    NoticeBoardQueryRepository noticeBoardQueryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardService boardService;
    @Autowired
    GroupBoardService groupBoardService;
    @Autowired
    GlannerService glannerService;
    @Autowired
    GlannerBoardService glannerBoardService;

    private final String userEmail = "test@test.com";

    @BeforeEach
    public void init(){
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

    /**
     *
     * 생성된 시간 순서로 offset과 limit으로 게시판 페이지를 가져오는 서비스
     */
    @Test
    public void testFindFreeBoards() throws Exception{
        //given
        for (int i = 0; i < 4; i++) {
            SaveFreeBoardReqDto boardReqDto = new SaveFreeBoardReqDto("title"+i, "content", new ArrayList<>());
            boardService.saveBoard(userEmail, boardReqDto);
            Thread.sleep(30);
        }

        //when
        List<FindFreeBoardResDto> page = freeBoardQueryRepository.findPage(0, 4);

        //then
        assertThat(page.size()).isEqualTo(4);
        assertThat(page.get(0).getTitle()).isEqualTo("title3");
    }

    @Test
    public void testFindNoticeBoards() throws Exception{
        //given
        for (int i = 0; i < 4; i++) {
            SaveNoticeBoardReqDto boardReqDto = new SaveNoticeBoardReqDto("title"+i, "content", new ArrayList<>());
            boardService.saveBoard(userEmail, boardReqDto);
            Thread.sleep(30);
        }

        //when
        List<FindNoticeBoardResDto> page = noticeBoardQueryRepository.findPage(0, 4);

        //then
        assertThat(page.size()).isEqualTo(4);
        assertThat(page.get(0).getTitle()).isEqualTo("title3");
    }

    @Test
    public void testFindGroupBoards() throws Exception{
        //given
        for (int i = 0; i < 4; i++) {
            SaveGroupBoardReqDto boardReqDto = new SaveGroupBoardReqDto("title"+i, "content", new ArrayList<>(), "interests");
            groupBoardService.saveGroupBoard(userEmail, boardReqDto);
        }

        //when
        List<FindGroupBoardResDto> page = groupBoardQueryRepository.findPage(0, 4);

        //then
        assertThat(page.size()).isEqualTo(4);
        assertThat(page.get(0).getTitle()).isEqualTo("title3");
        assertThat(page.get(0).getUserCount()).isEqualTo(1);
    }

    @Test
    public void testFindGlannerBoards() throws Exception{
        //given
        Long savedGlannerId = glannerService.saveGlanner(userEmail);
        for (int i = 0; i < 4; i++) {
            SaveGlannerBoardReqDto boardReqDto = new SaveGlannerBoardReqDto("title"+i, "content", new ArrayList<>(), savedGlannerId);
            glannerBoardService.saveGlannerBoard(userEmail, boardReqDto);
            Thread.sleep(30);
        }

        //when
        List<FindGlannerBoardResDto> page = glannerBoardQueryRepository.findPage(savedGlannerId,0, 4);

        //then
        assertThat(page.size()).isEqualTo(4);
        assertThat(page.get(0).getTitle()).isEqualTo("title3");
    }

    /**
     *
     * 검색 키워드와 offset과 limit으로 게시판 페이지를 가져오는 서비스
     */
    @Test
    public void testSearchGroupBoards() throws Exception{
        //given
        int page = 0;
        int limit = 5;
        SearchBoardReqDto reqDto = new SearchBoardReqDto("1");
        for (int i = 0; i < 10; i++) {
            groupBoardService.saveGroupBoard(userEmail, new SaveGroupBoardReqDto("title" + i, "content", new ArrayList<>(), "null"));
        }

        //when
        List<FindGroupBoardResDto> boardPage = groupBoardQueryRepository.findPageWithKeyword(page, limit, reqDto.getKeyWord());

        //then
        assertThat(boardPage.size()).isEqualTo(1);
        assertThat(boardPage.get(0).getTitle()).isEqualTo("title1");
    }

    @Test
    public void testSearchNoticeBoards() throws Exception{
        //given
        int page = 0;
        int limit = 5;
        SearchBoardReqDto reqDto = new SearchBoardReqDto("1");
        for (int i = 0; i < 10; i++) {
            boardService.saveBoard(userEmail, new SaveNoticeBoardReqDto("title" + i, "content", new ArrayList<>()));
        }

        //when
        List<FindNoticeBoardResDto> boardPage = noticeBoardQueryRepository.findPageWithKeyword(page, limit, reqDto.getKeyWord());

        //then
        assertThat(boardPage.size()).isEqualTo(1);
        assertThat(boardPage.get(0).getTitle()).isEqualTo("title1");
    }

    @Test
    public void testSearchFreeBoards() throws Exception{
        //given
        int page = 0;
        int limit = 5;
        SearchBoardReqDto reqDto = new SearchBoardReqDto("1");
        for (int i = 0; i < 10; i++) {
            boardService.saveBoard(userEmail, new SaveFreeBoardReqDto("title" + i, "content", new ArrayList<>()));
        }

        //when
        List<FindFreeBoardResDto> boardPage = freeBoardQueryRepository.findPageWithKeyword(page, limit, reqDto.getKeyWord());

        //then
        assertThat(boardPage.size()).isEqualTo(1);
        assertThat(boardPage.get(0).getTitle()).isEqualTo("title1");
    }

    @Test
    public void testSearchGlannerBoards() throws Exception{
        //given
        int page = 0;
        int limit = 5;
        SearchBoardReqDto reqDto = new SearchBoardReqDto("1");
        Long savedGlannerId = glannerService.saveGlanner(userEmail);
        for (int i = 0; i < 10; i++) {
            glannerBoardService.saveGlannerBoard(userEmail, new SaveGlannerBoardReqDto("title" + i, "content", new ArrayList<>(), savedGlannerId));
        }

        //when
        List<FindGlannerBoardResDto> boardPage = glannerBoardQueryRepository.findPageWithKeyword(savedGlannerId, page, limit, reqDto.getKeyWord());

        //then
        assertThat(boardPage.size()).isEqualTo(1);
        assertThat(boardPage.get(0).getTitle()).isEqualTo("title1");
    }



}
