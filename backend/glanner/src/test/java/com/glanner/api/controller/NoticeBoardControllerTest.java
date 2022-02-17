package com.glanner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.api.dto.response.FindNoticeBoardWithCommentResDto;
import com.glanner.api.queryrepository.NoticeBoardQueryRepository;
import com.glanner.api.service.NoticeBoardService;
import com.glanner.core.domain.board.NoticeBoard;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "cherish8514@naver.com", password = "1234")
public class NoticeBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NoticeBoardQueryRepository queryRepository;
    @MockBean
    private NoticeBoardService noticeBoardService;
    private final String userEmail = "cherish8514@naver.com";

    @Test
    public void testFindBoardOne() throws Exception{
        //given
        Long boardId = 1L;
        NoticeBoard noticeBoard = NoticeBoard.boardBuilder()
                .user(new User("name", "email", "password", "phoneNumber", UserRoleStatus.ROLE_USER))
                .title("title")
                .content("content")
                .build();
        List<FindCommentResDto> commentResDtos = new ArrayList<>();
        FindNoticeBoardWithCommentResDto boardResDto = new FindNoticeBoardWithCommentResDto(noticeBoard, commentResDtos);

        //when
        when(noticeBoardService.getNotice(boardId)).thenReturn(boardResDto);
        mockMvc.perform(get("/api/notice/{id}", boardId))

                //then
                .andExpect(status().isOk());
        verify(noticeBoardService, times(1)).getNotice(boardId);
    }

    @Test
    public void testFindBoardsPage() throws Exception{
        //given
        int page = 0;
        int limit = 25;

        //when
        mockMvc.perform(get("/api/notice/{page}/{limit}", page, limit))

                //then
                .andExpect(status().isOk());
        verify(queryRepository, times(1)).findPage(page, limit);
    }

    @Test
    public void testSearchBoardsPage() throws Exception{
        //given
        int page = 0;
        int limit = 25;
        SearchBoardReqDto reqDto = new SearchBoardReqDto("1");

        //when
        mockMvc.perform(get("/api/notice/search/{page}/{limit}", page, limit)
                        .param("keyword", "키워드"))
                //then
                .andExpect(status().isOk());

        verify(queryRepository, times(1))
                .findPageWithKeyword(page, limit, "키워드");
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
