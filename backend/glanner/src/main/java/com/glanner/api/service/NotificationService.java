package com.glanner.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.glanner.api.dto.request.SendMailReqDto;
import com.glanner.api.dto.request.SendSmsReqDto;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface NotificationService {
    public void sendMail(SendMailReqDto reqDto);
    public void sendSms(SendSmsReqDto reqDto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException, JsonProcessingException;
    public void sendScheduledSms();
}
