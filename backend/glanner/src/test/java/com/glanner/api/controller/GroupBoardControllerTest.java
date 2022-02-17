package com.glanner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glanner.api.dto.request.AddCommentReqDto;
import com.glanner.api.dto.request.SaveGroupBoardReqDto;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.api.dto.response.FindGroupBoardWithCommentResDto;
import com.glanner.api.queryrepository.GroupBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.GroupBoardService;
import com.glanner.core.domain.glanner.GroupBoard;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "cherish8514@naver.com", password = "1234")
public class GroupBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GroupBoardService groupBoardService;
    @MockBean
    private BoardService boardService;
    @MockBean
    private GroupBoardQueryRepository queryRepository;
    private final String userEmail = "cherish8514@naver.com";

    @Test
    public void testSaveFreeBoard() throws Exception{
        //given
        SaveGroupBoardReqDto reqDto = new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "#공부#운동#");

        //when
        mockMvc.perform(post("/api/group-board")
                .content(asJsonString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

        //then
                .andDo(print())
                .andExpect(status().isOk());
        verify(groupBoardService, times(1))
                .saveGroupBoard(eq(userEmail), any(SaveGroupBoardReqDto.class));
    }

    @Test
    public void testDeleteGroupBoard() throws Exception{
        //given
        Long deleteId = 1L;

        //when
        mockMvc.perform(delete("/api/group-board/{id}", deleteId))

        //then
                .andExpect(status().isOk());
        verify(boardService, times(1)).deleteBoard(eq(deleteId));
    }

    @Test
    public void testModifyGroupBoard() throws Exception{
        //given
        SaveGroupBoardReqDto reqDto = new SaveGroupBoardReqDto("title", "content", new ArrayList<>(), "#휴식#");

        //when
        mockMvc.perform(put("/api/group-board/{id}", 1L)
                .content(asJsonString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

        //then
                .andDo(print())
                .andExpect(status().isOk());
        verify(groupBoardService, times(1)).modifyGroupBoard(eq(1L), any(SaveGroupBoardReqDto.class));
    }

    @Test
    public void testAddComment() throws Exception{
        //given
        AddCommentReqDto reqDto = new AddCommentReqDto(1L, "content", null);

        //when
        mockMvc.perform(post("/api/group-board/comment")
                .content(asJsonString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

        //then
                .andDo(print())
                .andExpect(status().isOk());
        verify(boardService, times(1)).addComment(eq(userEmail), any(AddCommentReqDto.class));
    }

    @Test
    public void testFindBoardOne() throws Exception{
        //given
        Long boardId = 1L;
        GroupBoard groupBoard = GroupBoard.boardBuilder()
                .user(new User("name", "email", "password", "phoneNumber", UserRoleStatus.ROLE_USER))
                .title("title")
                .content("content")
                .interests("interests")
                .build();
        List<FindCommentResDto> commentResDtos = new ArrayList<>();
        FindGroupBoardWithCommentResDto groupBoardResDto = new FindGroupBoardWithCommentResDto(groupBoard, commentResDtos);

        //when
        when(groupBoardService.getGroupBoard(boardId)).thenReturn(groupBoardResDto);
        mockMvc.perform(get("/api/group-board/{id}", boardId))

        //then
                .andDo(print())
                .andExpect(status().isOk());
        verify(groupBoardService, times(1)).getGroupBoard(boardId);
    }

    @Test
    public void testFindBoardsPage() throws Exception{
        //given
        int page = 0;
        int limit = 25;

        //when
        mockMvc.perform(get("/api/group-board/{page}/{limit}", page, limit))

        //then
                .andDo(print())
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
        mockMvc.perform(get("/api/group-board/search/{page}/{limit}", page, limit)
                        .param("keyword", "키워드"))
                //then
                .andDo(print())
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
