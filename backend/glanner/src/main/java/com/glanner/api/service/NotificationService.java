package com.glanner.api.service;

import com.glanner.api.dto.request.SendMailReqDto;
import com.glanner.api.dto.request.SendSmsReqDto;
import com.glanner.api.dto.response.FindNotificationResDto;
import com.glanner.api.dto.response.SendSmsResDto;

import java.util.List;

public interface NotificationService {
    public List<FindNotificationResDto> findAll(String userEmail);
    public List<FindNotificationResDto> findUnread(String userEmail);
    public void modifyStatus(String userEmail);
    public void sendMail(SendMailReqDto reqDto);
    public SendSmsResDto sendSms(SendSmsReqDto reqDto);
    public void sendScheduledNoti();
}
