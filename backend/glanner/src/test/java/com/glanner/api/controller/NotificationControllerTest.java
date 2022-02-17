package com.glanner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.glanner.api.dto.request.SendMailReqDto;
import com.glanner.api.service.NotificationService;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    @WithMockUser(username = "cherish8514@naver.com", password = "1234")
    public void testFindNotifications() throws Exception{
        //given

        //when
        mockMvc.perform(get("/api/notification"))

        //then
                .andExpect(status().isOk());
        verify(notificationService, times(1)).findAll("cherish8514@naver.com");
    }

    @Test
    @WithMockUser(username = "cherish8514@naver.com", password = "1234")
    public void testFindUnReadNotifications() throws Exception{
        //given

        //when
        mockMvc.perform(get("/api/notification/unread"))

                //then
                .andExpect(status().isOk());
        verify(notificationService, times(1)).findUnread("cherish8514@naver.com");
    }

    @Test
    @WithMockUser(username = "cherish8514@naver.com", password = "1234")
    public void testModifyStatus() throws Exception{
        //given

        //when
        mockMvc.perform(post("/api/notification/status"))

                //then
                .andExpect(status().isOk());
        verify(notificationService, times(1)).modifyStatus("cherish8514@naver.com");
    }

    @Test
    public void testSendMail() throws Exception{
        //given
        SendMailReqDto reqDto = new SendMailReqDto("aldlswjddma@naver.com","title","content");

        //when
        mockMvc.perform(post("/api/notification/mail")
                .content(asJsonString(reqDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        //then
                .andExpect(status().isOk());
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