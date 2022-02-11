package com.glanner.api.service;

import com.glanner.api.dto.request.SendMailReqDto;
import com.glanner.api.dto.request.SendSmsReqDto;

public interface NotificationService {
    public void sendMail(SendMailReqDto reqDto);
    public void sendSms(SendSmsReqDto reqDto);
    public void sendScheduledSms();
}
