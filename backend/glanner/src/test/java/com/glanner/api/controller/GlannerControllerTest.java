package com.glanner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glanner.api.dto.request.AddGlannerWorkReqDto;
import com.glanner.api.dto.request.AddUserToGlannerReqDto;
import com.glanner.api.dto.request.ChangeGlannerNameReqDto;
import com.glanner.api.dto.response.FindGlannerWorkResDto;
import com.glanner.api.queryrepository.DailyWorkGlannerQueryRepository;
import com.glanner.api.service.GlannerService;
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

import java.time.LocalDateTime;
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
public class GlannerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GlannerService glannerService;
    @MockBean
    private DailyWorkGlannerQueryRepository dailyWorkQueryRepository;
    private final String userEmail = "cherish8514@naver.com";

    @Test
    public void testCreateGlanner() throws Exception{
        //given

        //when
        mockMvc.perform(post("/api/glanner"))

        //then
                .andExpect(status().isOk());
        verify(glannerService, times(1)).saveGlanner(any(String.class));
    }

    @Test
    public void testDeleteGlanner() throws Exception{
        //given

        //when
        mockMvc.perform(delete("/api/glanner/{id}", 1))

        //then
                .andExpect(status().isOk());
        verify(glannerService, times(1)).deleteGlanner(any(Long.class));
    }

    @Test
    public void testChangeGlannerName() throws Exception{
        //given
        ChangeGlannerNameReqDto reqDto = new ChangeGlannerNameReqDto(1L, "name");

        //when
        mockMvc.perform(put("/api/glanner/" )
                .content(asJsonString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isOk());
        verify(glannerService, times(1)).changeGlannerName(reqDto);
    }

    @Test
    public void testAddUser() throws Exception{
        //given
        AddUserToGlannerReqDto reqDto = new AddUserToGlannerReqDto(1L, userEmail);

        //when
        mockMvc.perform(post("/api/glanner/user")
                        .content(asJsonString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

        //then
                        .andExpect(status().isOk())
                        .andDo(print());
        verify(glannerService, times(1)).addUser(reqDto);
    }

    @Test
    public void testDeleteUser() throws Exception{
        //given

        //when
        mockMvc.perform(delete("/api/glanner/user/{glannerId}/{userId}", 1, 2))

        //then
                        .andExpect(status().isOk())
                        .andDo(print());
        verify(glannerService, times(1)).deleteUser(anyLong(), anyLong());
    }

    @Test
    public void testAddDailyWork() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ldt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());
        AddGlannerWorkReqDto reqDto = new AddGlannerWorkReqDto(1L, "title", "content", ldt, ldt, null);

        //when
        mockMvc.perform(post("/api/glanner/work")
                        .content(asJsonString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

        //then
                        .andExpect(status().isOk())
                        .andDo(print());
        verify(glannerService, times(1)).addDailyWork(reqDto);
    }

    @Test
    public void testFindDailyWork() throws Exception{
        //given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ldt = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), now.getMinute());

        List<FindGlannerWorkResDto> response = new ArrayList<>();
        response.add(new FindGlannerWorkResDto(1L,"title", "content", ldt, ldt, null));
        response.add(new FindGlannerWorkResDto(2L,"title", "content", ldt, ldt, null));


        //when
        when(dailyWorkQueryRepository.findByGlannerIdWithDate(1L, ldt, ldt)).thenReturn(response);
        mockMvc.perform(get("/api/glanner/work/{glannerId}/{startTime}/{endTime}", 1L, ldt, ldt))

        //then
                    .andExpect(status().isOk())
                    .andDo(print());
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
