package com.glanner.api.service;

import com.glanner.api.dto.request.*;
import com.glanner.api.dto.response.*;
import com.glanner.core.domain.board.FreeBoard;
import com.glanner.core.domain.board.NoticeBoard;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FreeBoardRepository freeBoardRepository;
    @Autowired
    NoticeBoardRepository noticeBoardRepository;
    @Autowired
    GlannerBoardRepository glannerBoardRepository;
    @Autowired
    GroupBoardRepository groupBoardRepository;

    @Autowired
    GlannerService glannerService;
    @Autowired
    FreeBoardService freeBoardService;
    @Autowired
    GlannerBoardService glannerBoardService;
    @Autowired
    GroupBoardService groupBoardService;
    @Autowired
    NoticeBoardService noticeBoardService;
    @Autowired
    BoardService boardService;
    @Autowired
    NotificationRepository notificationRepository;

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
     * 게시판 생성 테스트
     */
    @Test
    public void testCreateBoard() throws Exception{
        //given
        Long savedGlannerId = glannerService.saveGlanner(userEmail);
        SaveFreeBoardReqDto freeBoardReqDto1 = new SaveFreeBoardReqDto("title", "content", new ArrayList<>());
        SaveGroupBoardReqDto groupBoardReqDto = new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "interests");
        SaveGlannerBoardReqDto glannerBoardReqDto = new SaveGlannerBoardReqDto("title", "content", new ArrayList<>(), savedGlannerId);
        SaveNoticeBoardReqDto noticeBoardReqDto = new SaveNoticeBoardReqDto("title", "content", new ArrayList<>());

        //when
        Long freeBoardId = boardService.saveBoard(userEmail, freeBoardReqDto1);
        Long groupBoardId = boardService.saveBoard(userEmail, groupBoardReqDto);
        Long glannerBoardId = boardService.saveBoard(userEmail, glannerBoardReqDto);
        Long noticeId = boardService.saveBoard(userEmail, noticeBoardReqDto);

        //then
        assertThat(freeBoardId).isNotNull();
        assertThat(groupBoardId).isNotNull();
        assertThat(glannerBoardId).isNotNull();
        assertThat(noticeId).isNotNull();
    }

    /**
     * 게시판 수정 테스트
     */
    @Test
    public void testModifyBoard() throws Exception{
        //given
        Long savedGlannerId = glannerService.saveGlanner(userEmail);
        SaveFreeBoardReqDto freeBoardReqDto = new SaveFreeBoardReqDto("title", "content", new ArrayList<>());
        SaveGroupBoardReqDto groupBoardReqDto = new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "interests");
        SaveGlannerBoardReqDto glannerBoardReqDto = new SaveGlannerBoardReqDto("title", "content", new ArrayList<>(), savedGlannerId);
        SaveNoticeBoardReqDto noticeBoardReqDto = new SaveNoticeBoardReqDto("title", "content", new ArrayList<>());
        Long freeBoardId = boardService.saveBoard(userEmail, freeBoardReqDto);
        Long glannerBoardId = glannerBoardService.saveGlannerBoard(userEmail, glannerBoardReqDto);
        Long noticeId = boardService.saveBoard(userEmail, noticeBoardReqDto);

        SaveGroupBoardResDto groupBoardResDto = groupBoardService.saveGroupBoard(userEmail, groupBoardReqDto);

        SaveFreeBoardReqDto freeBoardModifyReqDto = new SaveFreeBoardReqDto("modify", "content", new ArrayList<>());
        SaveGroupBoardReqDto groupBoardModifyReqDto = new SaveGroupBoardReqDto("title", "modify", new ArrayList<>(), "interests");
        SaveGlannerBoardReqDto glannerBoardModifyReqDto = new SaveGlannerBoardReqDto("tt", "content", new ArrayList<>(), savedGlannerId);
        SaveNoticeBoardReqDto noticeBoardModifyReqDto = new SaveNoticeBoardReqDto("title", "cc", new ArrayList<>());

        //when
        boardService.modifyBoard(freeBoardId, freeBoardModifyReqDto);
        boardService.modifyBoard(groupBoardResDto.getGroupBoardId(), groupBoardModifyReqDto);
        boardService.modifyBoard(glannerBoardId, glannerBoardModifyReqDto);
        boardService.modifyBoard(noticeId, noticeBoardModifyReqDto);

        //then
        FreeBoard findFreeBoard = freeBoardRepository.findById(freeBoardId).orElseThrow(IllegalAccessError::new);
        NoticeBoard findNotice = noticeBoardRepository.findById(noticeId).orElseThrow(IllegalAccessError::new);
        GlannerBoard findGlannerBoard = glannerBoardRepository.findById(glannerBoardId).orElseThrow(IllegalAccessError::new);
        GroupBoard findGroupBoard = groupBoardRepository.findById(groupBoardResDto.getGroupBoardId()).orElseThrow(IllegalAccessError::new);
        assertThat(findFreeBoard.getTitle()).isEqualTo("modify");
        assertThat(findNotice.getContent()).isEqualTo("cc");
        assertThat(findGlannerBoard.getTitle()).isEqualTo("tt");
        assertThat(findGroupBoard.getContent()).isEqualTo("modify");
    }


    /**
     * 특정 게시판에 댓글을 추가하는 서비스
     */
    @Test
    public void testAddComment() throws Exception{
        //given
        Long savedGlannerId = glannerService.saveGlanner(userEmail);
        SaveFreeBoardReqDto boardReqDto1 = new SaveFreeBoardReqDto("title", "content", new ArrayList<>());
        SaveGroupBoardReqDto boardReqDto2 = new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "null");
        SaveNoticeBoardReqDto boardReqDto3 = new SaveNoticeBoardReqDto("title", "content", new ArrayList<>());
        SaveGlannerBoardReqDto boardReqDto4 = new SaveGlannerBoardReqDto("title", "content", new ArrayList<>(), savedGlannerId);

        Long freeBoardId = boardService.saveBoard(userEmail, boardReqDto1);
        SaveGroupBoardResDto groupBoardResDto = groupBoardService.saveGroupBoard(userEmail, boardReqDto2);
        Long noticeId = boardService.saveBoard(userEmail, boardReqDto3);
        Long glannerBoardId = glannerBoardService.saveGlannerBoard(userEmail, boardReqDto4);
        AddCommentReqDto addCommentReqDto1 = new AddCommentReqDto(freeBoardId, "content", null);
        AddCommentReqDto addCommentReqDto2 = new AddCommentReqDto(groupBoardResDto.getGroupBoardId(), "content", null);
        AddCommentReqDto addCommentReqDto3 = new AddCommentReqDto(noticeId, "content", null);
        AddCommentReqDto addCommentReqDto4 = new AddCommentReqDto(glannerBoardId, "content", null);

        //when
        boardService.addComment(userEmail, addCommentReqDto1);
        boardService.addComment(userEmail, addCommentReqDto2);
        boardService.addComment(userEmail, addCommentReqDto3);
        boardService.addComment(userEmail, addCommentReqDto4);
        //then
        FreeBoard findFreeBoard = freeBoardRepository.findById(freeBoardId).orElseThrow(IllegalAccessError::new);
        NoticeBoard findNoticeBoard = noticeBoardRepository.findById(noticeId).orElseThrow(IllegalAccessError::new);
        GroupBoard findGroupBoard = groupBoardRepository.findById(groupBoardResDto.getGroupBoardId()).orElseThrow(IllegalAccessError::new);
        GlannerBoard findGlannerBoard = glannerBoardRepository.findById(glannerBoardId).orElseThrow(IllegalAccessError::new);

        assertThat(findFreeBoard.getComments().size()).isEqualTo(1);
        assertThat(findFreeBoard.getComments().get(0).getContent()).isEqualTo("content");
        assertThat(findFreeBoard.getComments().get(0).getParent()).isEqualTo(null);
        assertThat(findNoticeBoard.getComments().size()).isEqualTo(1);
        assertThat(findNoticeBoard.getComments().get(0).getContent()).isEqualTo("content");
        assertThat(findNoticeBoard.getComments().get(0).getParent()).isEqualTo(null);
        assertThat(findGroupBoard.getComments().size()).isEqualTo(1);
        assertThat(findGroupBoard.getComments().get(0).getContent()).isEqualTo("content");
        assertThat(findGroupBoard.getComments().get(0).getParent()).isEqualTo(null);
        assertThat(findGlannerBoard.getComments().size()).isEqualTo(1);
        assertThat(findGlannerBoard.getComments().get(0).getContent()).isEqualTo("content");
        assertThat(findGlannerBoard.getComments().get(0).getParent()).isEqualTo(null);

        assertThat(findFreeBoard.getUser().getNotifications().size()).isEqualTo(0);
    }

    /**
     * 특정 게시판의 대댓글을 다는 서비스
     */
    @Test
    public void testAddChildComment() throws Exception{
        //given
        SaveFreeBoardReqDto boardReqDto1 = new SaveFreeBoardReqDto("title", "content", new ArrayList<>());
        Long freeBoardId = boardService.saveBoard(userEmail, boardReqDto1);
        AddCommentReqDto addCommentReqDto1 = new AddCommentReqDto(freeBoardId, "content", null);
        boardService.addComment(userEmail, addCommentReqDto1);
        Long parentId = freeBoardService.getFreeBoard(freeBoardId).getComments().get(0).getCommentId();

        //when
        AddCommentReqDto addChildCommentReqDto = new AddCommentReqDto(freeBoardId, "child", parentId);
        boardService.addComment(userEmail, addChildCommentReqDto);

        //then
        List<FindCommentResDto> comments = freeBoardService.getFreeBoard(freeBoardId).getComments();
        assertThat(comments.size()).isEqualTo(2);
    }
    /**
     * 특정 게시판의 댓글을 수정하는 서비스
     */
    @Test
    public void testModifyComment() throws Exception{
        //given
        Long savedGlannerId = glannerService.saveGlanner(userEmail);
        SaveFreeBoardReqDto boardReqDto1 = new SaveFreeBoardReqDto("title", "content", new ArrayList<>());
        SaveGroupBoardReqDto boardReqDto2 = new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "null");
        SaveNoticeBoardReqDto boardReqDto3 = new SaveNoticeBoardReqDto("title", "content", new ArrayList<>());
        SaveGlannerBoardReqDto boardReqDto4 = new SaveGlannerBoardReqDto("title", "content", new ArrayList<>(), savedGlannerId);

        Long freeBoardId = boardService.saveBoard(userEmail, boardReqDto1);
        SaveGroupBoardResDto groupBoardResDto = groupBoardService.saveGroupBoard(userEmail, boardReqDto2);
        Long noticeId = boardService.saveBoard(userEmail, boardReqDto3);
        Long glannerBoardId = glannerBoardService.saveGlannerBoard(userEmail, boardReqDto4);

        AddCommentReqDto addCommentReqDto1 = new AddCommentReqDto(freeBoardId, "content", null);
        AddCommentReqDto addCommentReqDto2 = new AddCommentReqDto(groupBoardResDto.getGroupBoardId(), "content", null);
        AddCommentReqDto addCommentReqDto3 = new AddCommentReqDto(noticeId, "content", null);
        AddCommentReqDto addCommentReqDto4 = new AddCommentReqDto(glannerBoardId, "content", null);

        Long commentId1 = boardService.addComment(userEmail, addCommentReqDto1).getCommentId();
        Long commentId2 = boardService.addComment(userEmail, addCommentReqDto2).getCommentId();
        Long commentId3 = boardService.addComment(userEmail, addCommentReqDto3).getCommentId();
        Long commentId4 = boardService.addComment(userEmail, addCommentReqDto4).getCommentId();

        //when
        UpdateCommentReqDto modifyCommentReqDto1 = new UpdateCommentReqDto("modify");
        UpdateCommentReqDto modifyCommentReqDto2 = new UpdateCommentReqDto("modify");
        UpdateCommentReqDto modifyCommentReqDto3 = new UpdateCommentReqDto("modify");
        UpdateCommentReqDto modifyCommentReqDto4 = new UpdateCommentReqDto("modify");

        boardService.modifyComment(commentId1, modifyCommentReqDto1);
        boardService.modifyComment(commentId2, modifyCommentReqDto2);
        boardService.modifyComment(commentId3, modifyCommentReqDto3);
        boardService.modifyComment(commentId4, modifyCommentReqDto4);

        //then
        FreeBoard findFreeBoard = freeBoardRepository.findById(freeBoardId).orElseThrow(IllegalAccessError::new);
        NoticeBoard findNoticeBoard = noticeBoardRepository.findById(noticeId).orElseThrow(IllegalAccessError::new);
        GroupBoard findGroupBoard = groupBoardRepository.findById(groupBoardResDto.getGroupBoardId()).orElseThrow(IllegalAccessError::new);
        GlannerBoard findGlannerBoard = glannerBoardRepository.findById(glannerBoardId).orElseThrow(IllegalAccessError::new);

        assertThat(findFreeBoard.getComments().get(0).getContent()).isEqualTo("modify");
        assertThat(findNoticeBoard.getComments().get(0).getContent()).isEqualTo("modify");
        assertThat(findGroupBoard.getComments().get(0).getContent()).isEqualTo("modify");
        assertThat(findGlannerBoard.getComments().get(0).getContent()).isEqualTo("modify");

    }


    /**
     * 특정 게시판의 정보를 dto로 반환하는 서비스
     */
    @Test
    public void testFindBoard() throws Exception{
        //given
        Long savedGlannerId = glannerService.saveGlanner(userEmail);
        SaveFreeBoardReqDto freeBoardReqDto1 = new SaveFreeBoardReqDto("title", "content", new ArrayList<>());
        SaveGroupBoardReqDto groupBoardReqDto = new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "interests");
        SaveGlannerBoardReqDto glannerBoardReqDto = new SaveGlannerBoardReqDto("title", "content", new ArrayList<>(), savedGlannerId);
        SaveNoticeBoardReqDto noticeBoardReqDto = new SaveNoticeBoardReqDto("title", "content", new ArrayList<>());
        Long freeBoardId = boardService.saveBoard(userEmail, freeBoardReqDto1);
        Long groupBoardId = boardService.saveBoard(userEmail, groupBoardReqDto);
        Long glannerBoardId = glannerBoardService.saveGlannerBoard(userEmail, glannerBoardReqDto);
        Long noticeId = boardService.saveBoard(userEmail, noticeBoardReqDto);

        //when
        FindFreeBoardWithCommentsResDto freeBoardResDto = freeBoardService.getFreeBoard(freeBoardId);
        FindGroupBoardWithCommentResDto groupBoardResDto = groupBoardService.getGroupBoard(groupBoardId);
        FindGlannerBoardWithCommentsResDto glannerBoardResDto = glannerBoardService.getGlannerBoard(glannerBoardId);
        FindNoticeBoardWithCommentResDto noticeResDto = noticeBoardService.getNotice(noticeId);

        //then
        assertThat(freeBoardResDto.getLikeCount()).isEqualTo(0);
        assertThat(groupBoardResDto.getInterests()).isEqualTo("interests");
        assertThat(glannerBoardResDto.getGlannerId()).isNotNull();
        assertThat(noticeResDto.getComments()).isNotNull();
        assertThat(freeBoardResDto.getContent()).isEqualTo("content");
        assertThat(groupBoardResDto.getContent()).isEqualTo("content");
        assertThat(glannerBoardResDto.getContent()).isEqualTo("content");
        assertThat(noticeResDto.getContent()).isEqualTo("content");
        assertThat(freeBoardResDto.getUserEmail()).isEqualTo(userEmail);
        assertThat(groupBoardResDto.getUserEmail()).isEqualTo(userEmail);
        assertThat(glannerBoardResDto.getUserEmail()).isEqualTo(userEmail);
        assertThat(noticeResDto.getUserEmail()).isEqualTo(userEmail);
    }

}
