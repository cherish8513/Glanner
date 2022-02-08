package com.glanner.api.service;

import com.glanner.api.dto.request.AddCommentReqDto;
import com.glanner.api.dto.request.SaveFreeBoardReqDto;
import com.glanner.api.dto.response.FindFreeBoardResDto;
import com.glanner.api.queryrepository.FreeBoardQueryRepository;
import com.glanner.core.domain.board.Board;
import com.glanner.core.domain.board.Comment;
import com.glanner.core.domain.board.FreeBoard;
import com.glanner.core.domain.board.NoticeBoard;
import com.glanner.core.domain.user.DailyWorkSchedule;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.BoardRepository;
import com.glanner.core.repository.CommentRepository;
import com.glanner.core.repository.FreeBoardRepository;
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
public class BoardServiceTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    FreeBoardQueryRepository freeBoardQueryRepository;

    @Autowired
    FreeBoardRepository freeBoardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void init(){
        createUser();
    }

    @Test
    public void testCreateBoard() throws Exception{
        //given
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(IllegalAccessError::new);
        SaveFreeBoardReqDto freeBoardReqDto = new SaveFreeBoardReqDto("title", "content", null);

        FreeBoard freeBoard = freeBoardReqDto.toEntity(findUser);
        NoticeBoard noticeBoard = NoticeBoard.boardBuilder().build();

        //when
        FreeBoard savedFreeBoard = boardRepository.save(freeBoard);
        boardRepository.save(noticeBoard);

        //then
        assertThat(savedFreeBoard.getDislikeCount()).isEqualTo(0);
        assertThat(savedFreeBoard.getLikeCount()).isEqualTo(0);
        assertThat(savedFreeBoard.getContent()).isEqualTo("content");
        assertThat(savedFreeBoard.getTitle()).isEqualTo("title");
        assertThat(savedFreeBoard.getCount()).isEqualTo(0);
    }

    /**
     * 특정 게시판에 댓글을 추가하는 서비스
     */
    @Test
    public void testAddComment() throws Exception{
        //given
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(IllegalAccessError::new);
        SaveFreeBoardReqDto freeBoardReqDto1 = new SaveFreeBoardReqDto("title", "content", null);
        FreeBoard freeBoard1 = freeBoardReqDto1.toEntity(findUser);
        Long savedBoardId = boardRepository.save(freeBoard1).getId();
        AddCommentReqDto addCommentReqDto = new AddCommentReqDto(savedBoardId, "content", null);

        //when
        Board board = boardRepository.findById(savedBoardId).orElseThrow(IllegalAccessError::new);
        Comment parent = null;
        if(addCommentReqDto.getParentId() != null){
            parent = commentRepository.findById(addCommentReqDto.getParentId()).orElseThrow(IllegalArgumentException::new);
        }
        Comment comment = new Comment(addCommentReqDto.getContent(), findUser, parent, board);
        board.addComment(comment);

        //then
        FreeBoard findFreeBoard = freeBoardRepository.findById(savedBoardId).orElseThrow(IllegalAccessError::new);
        assertThat(findFreeBoard.getComments().size()).isEqualTo(1);
        assertThat(findFreeBoard.getComments().get(0).getContent()).isEqualTo("content");
        assertThat(findFreeBoard.getComments().get(0).getParent()).isEqualTo(null);
    }


    /**
     * 특정 게시판의 정보를 dto로 반환하는 서비스
     */
    @Test
    public void testFindBoard() throws Exception{
        //given
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(IllegalAccessError::new);
        SaveFreeBoardReqDto freeBoardReqDto1 = new SaveFreeBoardReqDto("title", "content", null);
        FreeBoard freeBoard1 = freeBoardReqDto1.toEntity(findUser);
        Long savedBoardId = boardRepository.save(freeBoard1).getId();

        //when
        FindFreeBoardResDto findFreeBoardResDto = freeBoardQueryRepository.findById(savedBoardId).orElseThrow(IllegalAccessError::new);

        //then
        assertThat(findFreeBoardResDto.getLikeCount()).isEqualTo(0);
    }

    /**
     *
     * 생성된 시간 순서로 offset과 limit으로 게시판 페이지를 가져오는 서비스
     */
    @Test
    public void testFindBoards() throws Exception{
        //given
        User findUser = userRepository.findByEmail("cherish8513@naver.com").orElseThrow(IllegalAccessError::new);
        SaveFreeBoardReqDto freeBoardReqDto1 = new SaveFreeBoardReqDto("title1", "content", null);
        SaveFreeBoardReqDto freeBoardReqDto2 = new SaveFreeBoardReqDto("title2", "content", null);
        SaveFreeBoardReqDto freeBoardReqDto3 = new SaveFreeBoardReqDto("title3", "content", null);
        SaveFreeBoardReqDto freeBoardReqDto4 = new SaveFreeBoardReqDto("title4", "content", null);
        FreeBoard freeBoard1 = freeBoardReqDto1.toEntity(findUser);
        FreeBoard freeBoard2 = freeBoardReqDto2.toEntity(findUser);
        FreeBoard freeBoard3 = freeBoardReqDto3.toEntity(findUser);
        FreeBoard freeBoard4 = freeBoardReqDto4.toEntity(findUser);
        boardRepository.save(freeBoard1);
        boardRepository.save(freeBoard2);
        boardRepository.save(freeBoard3);
        boardRepository.save(freeBoard4);

        //when
        List<FindFreeBoardResDto> page = freeBoardQueryRepository.findPage(1, 3);

        //then
        assertThat(page.size()).isEqualTo(3);
        assertThat(page.get(0).getTitle()).isEqualTo("title3");
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
