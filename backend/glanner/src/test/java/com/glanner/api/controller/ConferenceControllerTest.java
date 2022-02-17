package com.glanner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glanner.api.dto.request.AddParticipantReqDto;
import com.glanner.api.dto.request.OpenConferenceReqDto;
import com.glanner.api.service.ConferenceService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "cherish8514@naver.com", password = "1234")
public class ConferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ConferenceService conferenceService;

    private final String userEmail = "cherish8514@naver.com";

    @Test
    public void testOpenConference() throws Exception{
        //given
        OpenConferenceReqDto reqDto = new OpenConferenceReqDto(1L, 6);
        //when
        mockMvc.perform(post("/api/conference")
                        .content(asJsonString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

        //then
                .andExpect(status().isOk());
        verify(conferenceService, times(1))
                .openConference(eq(userEmail), any(OpenConferenceReqDto.class));
    }

    @Test
    public void testCloseConference() throws Exception{
        //given
        //when
        mockMvc.perform(delete("/api/conference/{id}", 1L))

        //then
                .andExpect(status().isOk());

        verify(conferenceService, times(1))
                .closeConference(1L);
    }

    @Test
    public void testAddParticipant() throws Exception{
        //given
        AddParticipantReqDto reqDto = new AddParticipantReqDto(1L, 1L);
        //when
        mockMvc.perform(post("/api/conference/attendee")
                        .content(asJsonString(reqDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

        //then
                .andExpect(status().isOk());

        verify(conferenceService, times(1))
                .addParticipant(any(AddParticipantReqDto.class));
    }

    @Test
    public void testFindConference() throws Exception{
        //given

        //when
        mockMvc.perform(get("/api/conference/{id}",1L))

        //then
                .andExpect(status().isOk());

        verify(conferenceService, times(1))
                .findConferenceDetail(1L);
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
