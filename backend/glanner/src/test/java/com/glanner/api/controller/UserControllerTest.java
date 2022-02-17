package com.glanner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glanner.api.dto.request.AddPlannerWorkReqDto;
import com.glanner.api.dto.request.SaveUserReqDto;
import com.glanner.api.dto.response.FindPlannerWorkResDto;
import com.glanner.api.queryrepository.UserQueryRepository;
import com.glanner.api.service.UserService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "cherish8514@naver.com", password = "1234")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserQueryRepository userQueryRepository;

    private final String userEmail = "cherish8514@naver.com";

    @Test
    public void testJoin() throws Exception{
        //given
        SaveUserReqDto reqDto = SaveUserReqDto.builder()
                .email("cherish8514@naver.com")
                .name("싸피")
                .password("1234")
                .phoneNumber("010-1234-5678")
                .build();


        //when
        mockMvc.perform(post("/api/user")
                .content(asJsonString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

        //then
                .andExpect(status().isOk());
        verify(userService, times(1)).saveUser(any(SaveUserReqDto.class));
    }

    @Test
    public void testFindWorksList() throws Exception{
        //given
        String date = "2022-02-01";
        LocalDateTime dateTimeStart = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay().minusMinutes(1);
        LocalDateTime dateTimeEnd = dateTimeStart.plusMonths(1);
        FindPlannerWorkResDto success = new FindPlannerWorkResDto(1L,"title", "content", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null);
        List<FindPlannerWorkResDto> response = new ArrayList<>();
        response.add(success);

        //when
        when(userService.getWorks("cherish8513@naver.com", dateTimeStart, dateTimeEnd)).thenReturn(response);
        mockMvc.perform(get("/api/user/planner/{date}", date))

        //then
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService, times(1)).getWorks(userEmail, dateTimeStart, dateTimeEnd);
    }

    @Test
    public void testFindWork() throws Exception{
        //given
        Long workId = 1L;
        FindPlannerWorkResDto success = new FindPlannerWorkResDto(workId, "title", "content", LocalDateTime.now(), LocalDateTime.now().plusDays(3), null);

        //when
        when(userQueryRepository.findDailyWork(workId)).thenReturn(Optional.of(success));
        mockMvc.perform(get("/api/user/planner/work/{id}", workId))

                //then
                .andDo(print())
                .andExpect(status().isOk());
        verify(userQueryRepository, times(1)).findDailyWork(workId);
    }

    @Test
    public void testAddWork() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        AddPlannerWorkReqDto reqDto = new AddPlannerWorkReqDto("title", "content", now, now.plusDays(3), null);

        //when
        mockMvc.perform(post("/api/user/planner/work")
                .content(asJsonString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService, times(1)).addWork(userEmail, reqDto);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}