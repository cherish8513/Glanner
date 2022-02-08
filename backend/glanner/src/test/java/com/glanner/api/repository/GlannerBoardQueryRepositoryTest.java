package com.glanner.api.repository;

import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindGlannerBoardResDto;
import com.glanner.api.queryrepository.GlannerBoardQueryRepository;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GlannerBoard;
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

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class GlannerBoardQueryRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GlannerRepository glannerRepository;

    @Autowired
    private GlannerBoardQueryRepository queryRepository;

    private Long glannerId;

    @BeforeEach
    public void init(){
        createUser();
        glannerId = createGlanner();
        createGlannerBoard(glannerId, "title1");
        createGlannerBoard(glannerId, "title2");
        createGlannerBoard(glannerId, "title3");
        createGlannerBoard(glannerId, "title4");
        createGlannerBoard(glannerId, "title5");
        createGlannerBoard(glannerId, "title6");
        createGlannerBoard(glannerId, "title7");
        createGlannerBoard(glannerId, "title8");
        createGlannerBoard(glannerId, "title9");
        createGlannerBoard(glannerId, "title10");
    }


    /**
     *
     * 생성된 시간 순서로 offset과 limit으로 게시판 페이지를 가져오는 서비스
     */
    @Test
    public void testFindBoards() throws Exception{
        //given
        int offset = 0;
        int limit = 5;
        SearchBoardReqDto reqDto = new SearchBoardReqDto("5");

        //when
        List<FindGlannerBoardResDto> results = queryRepository.findByKeyWord(glannerId, offset, limit, reqDto);

        //then
        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getTitle()).isEqualTo("title5");
    }

    /**
     *
     * 검색할 키워드와 offset과 limit으로 게시판 페이지를 가져오는 서비스
     */
    @Test
    public void testSearchBoards() throws Exception{
        //given
        int offset = 0;
        int limit = 5;

        //when
        List<FindGlannerBoardResDto> results = queryRepository.findPage(glannerId, offset, limit);

        //then
        assertThat(results.size()).isEqualTo(5);
        assertThat(results.get(0).getTitle()).isEqualTo("title10");
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

    private void createGlannerBoard(Long glannerId, String title) {
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(() -> new IllegalStateException("없는 회원 입니다."));
        Glanner findGlanner = glannerRepository.findRealById(glannerId).orElseThrow(IllegalArgumentException::new);
        GlannerBoard glannerBoard = GlannerBoard.boardBuilder()
                .title(title)
                .content("content")
                .user(findUser)
                .build();
        findGlanner.addGlannerBoard(glannerBoard);
    }
}
