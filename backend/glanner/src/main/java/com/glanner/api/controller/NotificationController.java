package com.glanner.api.controller;

import com.glanner.api.dto.request.SendMailReqDto;
import com.glanner.api.dto.request.SendSmsReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindNotificationResDto;
import com.glanner.api.dto.response.SendSmsResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.service.NotificationService;
import com.glanner.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<FindNotificationResDto>> findNotifications(){
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        List<FindNotificationResDto> responseDto = notificationService.findAll(userEmail);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<FindNotificationResDto>> findUnreadNotifications(){
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        List<FindNotificationResDto> responseDto = notificationService.findUnread(userEmail);
        return ResponseEntity.status(200).body(responseDto);
    }

    @ApiOperation(value = "알림 상태 변경", notes = "'STILL_NOT_CONFIRM'인 알림 상태를 'CONFIRM' 으로 전환한다.")
    @PostMapping("/status")
    public ResponseEntity<BaseResponseEntity> modifyStatus(){
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        notificationService.modifyStatus(userEmail);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PostMapping("/mail")
    public ResponseEntity<BaseResponseEntity> sendMail(@RequestBody SendMailReqDto reqDto){
        notificationService.sendMail(reqDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PostMapping("/sms")
    public ResponseEntity<SendSmsResDto> sendSMS(@RequestBody SendSmsReqDto reqDto){
        SendSmsResDto resDto = notificationService.sendSms(reqDto);
        return ResponseEntity.status(200).body(resDto);
    }

    @Scheduled(cron="0 0/1 * * * *")
    public void sendScheduledNoti(){
        notificationService.sendScheduledNoti();
    }

    public String getUsername(Optional<String> username){
        return username.orElseThrow(UserNotFoundException::new);
    }

}
