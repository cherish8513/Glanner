package com.glanner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glanner.api.dto.request.SaveGlannerBoardReqDto;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindCommentResDto;
import com.glanner.api.dto.response.FindGlannerBoardWithCommentsResDto;
import com.glanner.api.queryrepository.GlannerBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.api.service.GlannerBoardService;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.glanner.GlannerBoard;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "cherish8514@naver.com", password = "1234")
public class GlannerBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GlannerBoardService glannerBoardService;
    @MockBean
    private BoardService boardService;
    @MockBean
    private GlannerBoardQueryRepository queryRepository;
    private final String userEmail = "cherish8514@naver.com";

    @Test
    public void testSaveGlannerBoard() throws Exception{
        //given
        SaveGlannerBoardReqDto reqDto = new SaveGlannerBoardReqDto("title", "content", null, 1L);

        //when
        mockMvc.perform(post("/api/glanner-board")
                        .content(asJsonString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

        //then
                .andExpect(status().isOk());
        verify(glannerBoardService, times(1))
                .saveGlannerBoard(eq(userEmail), any(SaveGlannerBoardReqDto.class));
        verify(boardService, times(0)).saveBoard(anyString(), any(SaveGlannerBoardReqDto.class));
    }

    @Test
    public void testDeleteGlannerBoard() throws Exception{
        //given
        Long deleteId = 1L;

        //when
        mockMvc.perform(delete("/api/glanner-board/{id}", deleteId))

        //then
                .andExpect(status().isOk());
        verify(boardService, times(1))
                .deleteBoard(eq(deleteId));
    }

    @Test
    public void testModifyGlannerBoard() throws Exception{
        //given
        SaveGlannerBoardReqDto reqDto = new SaveGlannerBoardReqDto("title", "comment", null, 1L);

        //when
        mockMvc.perform(put("/api/glanner-board/{id}", 1L)
                .content(asJsonString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isOk());
        verify(boardService, times(1)).modifyBoard(eq(1L), any(SaveGlannerBoardReqDto.class));
    }

    @Test
    public void testFindBoardsPage() throws Exception{
        //given
        Long glannerId = 1L;
        int page = 0;
        int limit = 25;

        //when
        mockMvc.perform(get("/api/glanner-board/{id}/{page}/{limit}", glannerId, page, limit))

                //then
                .andExpect(status().isOk());
        verify(glannerBoardService, times(1)).getGlannerBoards(glannerId, page, limit);
    }

    @Test
    public void testFindBoard() throws Exception{
        //given
        Long boardId = 1L;
        GlannerBoard board = GlannerBoard.boardBuilder()
                .user(new User("name", "email", "password", "phoneNumber", UserRoleStatus.ROLE_USER))
                .title("title")
                .content("content")
                .glanner(new Glanner(new User("name", "email", "password", "phoneNumber", UserRoleStatus.ROLE_USER), "glannerName"))
                .build();
        List<FindCommentResDto> commentResDtos = new ArrayList<>();
        FindGlannerBoardWithCommentsResDto boardResDto = new FindGlannerBoardWithCommentsResDto(board, commentResDtos);

        //when
        when(glannerBoardService.getGlannerBoard(boardId)).thenReturn(boardResDto);
        mockMvc.perform(get("/api/glanner-board/{id}", boardId))

                //then
                .andDo(print())
                .andExpect(status().isOk());
        verify(glannerBoardService, times(1)).getGlannerBoard(boardId);
    }

    @Test
    public void testSearchBoardsPage() throws Exception{
        //given
        Long glannerId = 1L;
        int page = 0;
        int limit = 25;
        SearchBoardReqDto reqDto = new SearchBoardReqDto("1");

        //when
        mockMvc.perform(get("/api/glanner-board/{id}/search/{page}/{limit}", glannerId, page, limit)
                        .param("keyword", "키워드"))

                //then
                .andExpect(status().isOk());
        verify(queryRepository, times(1))
                .findPageWithKeyword(glannerId, page, limit, "키워드");
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
