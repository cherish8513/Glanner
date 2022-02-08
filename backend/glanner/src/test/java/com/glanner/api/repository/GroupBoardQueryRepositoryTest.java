package com.glanner.api.repository;

import com.glanner.api.dto.request.SaveGroupBoardReqDto;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindGroupBoardResDto;
import com.glanner.api.queryrepository.GroupBoardQueryRepository;
import com.glanner.api.service.GroupBoardService;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class GroupBoardQueryRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupBoardQueryRepository queryRepository;

    @Autowired
    private GroupBoardService groupBoardService;

    private String userEmail = "cherish8513@naver.com";

    @BeforeEach
    public void init(){
        createUser();
        for (int i = 0; i < 10; i++) {
        groupBoardService.saveGroupBoard(userEmail, new SaveGroupBoardReqDto("title" + i, "content", new ArrayList<>(), null));
        }
    }


    /**
     *
     * 생성된 시간 순서로 offset과 limit으로 게시판 페이지를 가져오는 서비스
     */
    @Test
    public void testFindBoards() throws Exception{
        //given
        int page = 0;
        int limit = 5;

        //when
        List<FindGroupBoardResDto> boardPage = queryRepository.findPage(page, limit);

        //then
        assertThat(boardPage.size()).isEqualTo(5);
        assertThat(boardPage.get(0).getTitle()).isEqualTo("title9");
    }

    /**
     *
     * 검색 키워드와 offset과 limit으로 게시판 페이지를 가져오는 서비스
     */
    @Test
    public void testSearchBoards() throws Exception{
        //given
        int page = 0;
        int limit = 5;
        SearchBoardReqDto reqDto = new SearchBoardReqDto("1");

        //when
        List<FindGroupBoardResDto> boardPage = queryRepository.findByKeyWord(page, limit, reqDto);

        //then
        assertThat(boardPage.size()).isEqualTo(1);
        assertThat(boardPage.get(0).getTitle()).isEqualTo("title1");
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
        em.flush();
        em.clear();
    }
}
