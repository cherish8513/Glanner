package com.glanner.api.service;

import com.glanner.api.dto.request.AddPlannerWorkReqDto;
import com.glanner.api.dto.request.ChangePasswordReqDto;
import com.glanner.api.dto.request.SaveUserReqDto;
import com.glanner.api.dto.response.FindPlannerWorkResDto;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    Long saveUser(SaveUserReqDto reqDto);
    void delete(String userEmail);
    void changePassword(String userEmail, ChangePasswordReqDto requestDto);
    List<FindPlannerWorkResDto> getWorks(String userEmail, LocalDateTime start, LocalDateTime end);
    void addWork(String userEmail, AddPlannerWorkReqDto requestDto);
    void modifyWork(Long id, AddPlannerWorkReqDto requestDto);
}
