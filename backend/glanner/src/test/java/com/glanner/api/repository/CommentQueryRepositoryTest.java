package com.glanner.api.repository;

import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.api.queryrepository.CommentQueryRepository;
import com.glanner.core.domain.board.Comment;
import com.glanner.core.domain.board.FreeBoard;
import com.glanner.core.domain.board.NoticeBoard;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GlannerBoard;
import com.glanner.core.domain.glanner.GroupBoard;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class CommentQueryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupBoardRepository groupBoardRepository;
    @Autowired
    private FreeBoardRepository freeBoardRepository;
    @Autowired
    private GlannerBoardRepository glannerBoardRepository;
    @Autowired
    private GlannerRepository glannerRepository;
    @Autowired
    private NoticeBoardRepository noticeBoardRepository;

    @Autowired
    private CommentQueryRepository commentQueryRepository;

    private final String userEmail = "test@naver.com";
    private User user;

    @BeforeEach
    public void init(){
        user = createUser();
    }
    
    @Test
    public void testFindGroupBoardComments() throws Exception{
        //given
        Long boardId = groupBoardRepository.save(GroupBoard
                .boardBuilder()
                .title("title")
                .content("content")
                .user(user)
                .interests(null)
                .build()
        ).getId();
        GroupBoard groupBoard = groupBoardRepository.findRealById(boardId).orElseThrow(IllegalAccessError::new);
        User user = userRepository.findByEmail(userEmail).orElseThrow(IllegalArgumentException::new);
        for (int i = 0; i < 5; i++) {
            groupBoard.addComment(Comment.builder()
                    .content("content")
                    .parent(null)
                    .user(user)
                    .build());
        }
        Comment parent = Comment.builder()
                .content("content")
                .parent(null)
                .user(user)
                .build();
        groupBoard.addComment(parent);
        for (int i = 0; i < 3; i++) {
            groupBoard.addComment(Comment.builder()
                    .content("content")
                    .parent(parent)
                    .user(user)
                    .build());
        }
        //when
        List<FindCommentResDto> commentsByBoardId = commentQueryRepository.findCommentsByBoardId(boardId);

        //then
        assertThat(commentsByBoardId.size()).isEqualTo(9);
        int hasParent = 0;
        for (int i = 0; i < commentsByBoardId.size(); i++) {
            if(commentsByBoardId.get(i).getParentId() != -1)
                hasParent++;
        }
        assertThat(hasParent).isEqualTo(3);
    }

    @Test
    public void testFindFreeBoardComment() throws Exception{
        //given
        Long boardId = freeBoardRepository.save(FreeBoard
                .boardBuilder()
                .title("title")
                .content("content")
                .user(user)
                .build()
        ).getId();
        FreeBoard freeBoard = freeBoardRepository.findRealById(boardId).orElseThrow(IllegalAccessError::new);
        User user = userRepository.findByEmail(userEmail).orElseThrow(IllegalArgumentException::new);
        for (int i = 0; i < 5; i++) {
            freeBoard.addComment(Comment.builder()
                    .content("content")
                    .parent(null)
                    .user(user)
                    .build());
        }
        Comment parent = Comment.builder()
                .content("content")
                .parent(null)
                .user(user)
                .build();
        freeBoard.addComment(parent);
        for (int i = 0; i < 3; i++) {
            freeBoard.addComment(Comment.builder()
                    .content("content")
                    .parent(parent)
                    .user(user)
                    .build());
        }
        //when
        List<FindCommentResDto> commentsByBoardId = commentQueryRepository.findCommentsByBoardId(boardId);

        //then
        assertThat(commentsByBoardId.size()).isEqualTo(9);
        int hasParent = 0;
        for (FindCommentResDto findCommentResDto : commentsByBoardId) {
            if (findCommentResDto.getParentId() != -1)
                hasParent++;
        }
        assertThat(hasParent).isEqualTo(3);
    }

    @Test
    public void testFindGlannerBoardComment() throws Exception{
        //given
        Glanner glanner = glannerRepository.save(Glanner.builder().host(user).build());
        Long boardId = glannerBoardRepository.save(GlannerBoard
                .boardBuilder()
                .title("title")
                .content("content")
                .user(user)
                .glanner(glanner)
                .build()
        ).getId();
        GlannerBoard glannerBoard = glannerBoardRepository.findRealById(boardId).orElseThrow(IllegalAccessError::new);
        User user = userRepository.findByEmail(userEmail).orElseThrow(IllegalArgumentException::new);
        for (int i = 0; i < 5; i++) {
            glannerBoard.addComment(Comment.builder()
                    .content("content")
                    .parent(null)
                    .user(user)
                    .build());
        }
        Comment parent = Comment.builder()
                .content("content")
                .parent(null)
                .user(user)
                .build();
        glannerBoard.addComment(parent);
        for (int i = 0; i < 3; i++) {
            glannerBoard.addComment(Comment.builder()
                    .content("content")
                    .parent(parent)
                    .user(user)
                    .build());
        }
        //when
        List<FindCommentResDto> commentsByBoardId = commentQueryRepository.findCommentsByBoardId(boardId);

        //then
        assertThat(commentsByBoardId.size()).isEqualTo(9);
        int hasParent = 0;
        for (FindCommentResDto findCommentResDto : commentsByBoardId) {
            if (findCommentResDto.getParentId() != -1)
                hasParent++;
        }
        assertThat(hasParent).isEqualTo(3);
    }


    @Test
    public void testFindNoticeBoardComment() throws Exception{
        //given
        Long boardId = noticeBoardRepository.save(NoticeBoard
                .boardBuilder()
                .title("title")
                .content("content")
                .user(user)
                .build()
        ).getId();
        NoticeBoard noticeBoard = noticeBoardRepository.findRealById(boardId).orElseThrow(IllegalAccessError::new);
        User user = userRepository.findByEmail(userEmail).orElseThrow(IllegalArgumentException::new);
        for (int i = 0; i < 5; i++) {
            noticeBoard.addComment(Comment.builder()
                    .content("content")
                    .parent(null)
                    .user(user)
                    .build());
        }
        Comment parent = Comment.builder()
                .content("content")
                .parent(null)
                .user(user)
                .build();
        noticeBoard.addComment(parent);
        for (int i = 0; i < 3; i++) {
            noticeBoard.addComment(Comment.builder()
                    .content("content")
                    .parent(parent)
                    .user(user)
                    .build());
        }
        //when
        List<FindCommentResDto> commentsByBoardId = commentQueryRepository.findCommentsByBoardId(boardId);

        //then
        assertThat(commentsByBoardId.size()).isEqualTo(9);
        int hasParent = 0;
        for (FindCommentResDto findCommentResDto : commentsByBoardId) {
            if (findCommentResDto.getParentId() != -1)
                hasParent++;
        }
        assertThat(hasParent).isEqualTo(3);
    }


    public User createUser(){
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
        return userRepository.save(user);
    }
}
