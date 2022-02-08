package com.glanner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glanner.api.dto.request.SaveFreeBoardReqDto;
import com.glanner.api.dto.request.SearchBoardReqDto;
import com.glanner.api.dto.response.FindFreeBoardResDto;
import com.glanner.api.queryrepository.FreeBoardQueryRepository;
import com.glanner.api.service.BoardService;
import com.glanner.core.domain.user.Schedule;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserRoleStatus;
import com.glanner.core.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "cherish8514@naver.com", password = "1234")
public class FreeBoardControllerTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BoardService boardService;
    @MockBean
    private FreeBoardQueryRepository freeBoardQueryRepository;

    private final String userEmail = "cherish8514@naver.com";

    @BeforeTransaction
    public void init() {
        User user = User.builder()
                .phoneNumber("010-6575-2938")
                .email("cherish8514@naver.com")
                .name("JeongJooHeon")
                .password(passwordEncoder.encode("1234"))
                .role(UserRoleStatus.ROLE_USER)
                .build();

        Schedule schedule = Schedule.builder()
                .build();
        user.changeSchedule(schedule);
        userRepository.save(user);
    }

    @Test
    public void testSaveFreeBoard() throws Exception{
        //given
        SaveFreeBoardReqDto saveFreeBoardReqDto = new SaveFreeBoardReqDto("title", "content", null);

        //when
        mockMvc.perform(post("/api/free-board")
                .content(asJsonString(saveFreeBoardReqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

        //then
                .andExpect(status().isOk());
        verify(boardService, times(1))
                .saveBoard(eq(userEmail), any(SaveFreeBoardReqDto.class));
    }

    @Test
    public void testFindBoardOne() throws Exception{
        //given
        Long boardId = 1L;
        FindFreeBoardResDto findFreeBoardResDto =
                new FindFreeBoardResDto("title", "content", 0, 0, 0);

        //when
        when(freeBoardQueryRepository.findById(boardId)).thenReturn(Optional.of(findFreeBoardResDto));
        mockMvc.perform(get("/api/free-board/{id}", boardId))

                //then
                .andExpect(status().isOk());
        verify(freeBoardQueryRepository, times(1)).findById(boardId);
    }

    @Test
    public void testFindBoardsPage() throws Exception{
        //given
        int page = 1;
        int limit = 25;

        //when
        mockMvc.perform(get("/api/free-board/{page}/{limit}", page, limit))

        //then
                .andExpect(status().isOk());
        verify(freeBoardQueryRepository, times(1)).findPage(page, limit);
    }

    @Test
    public void testSearchBoardsPage() throws Exception{
        //given
        int page = 1;
        int limit = 25;
        SearchBoardReqDto reqDto = new SearchBoardReqDto("1");

        //when
        mockMvc.perform(get("/api/free-board/search/{page}/{limit}", page, limit)
                .content(asJsonString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isOk());
        verify(freeBoardQueryRepository, times(1))
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
