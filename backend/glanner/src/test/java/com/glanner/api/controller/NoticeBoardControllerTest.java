package com.glanner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindNoticeBoardResDto;
import com.glanner.api.queryrepository.NoticeBoardQueryRepository;
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

import java.util.Optional;

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
    private final String userEmail = "cherish8514@naver.com";

    @Test
    public void testFindBoardOne() throws Exception{
        //given
        Long boardId = 1L;
        FindNoticeBoardResDto noticeBoardResDto = new FindNoticeBoardResDto("title", "content", 0);

        //when
        when(queryRepository.findById(boardId)).thenReturn(Optional.of(noticeBoardResDto));
        mockMvc.perform(get("/api/notice/{id}", boardId))

                //then
                .andExpect(status().isOk());
        verify(queryRepository, times(1)).findById(boardId);
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
                        .content(asJsonString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk());

        verify(queryRepository, times(1))
                .findByKeyWord(eq(page), eq(limit), any(SearchBoardReqDto.class));
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
