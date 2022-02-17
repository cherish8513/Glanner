package com.glanner.api.service;

import com.glanner.api.dto.request.AddGlannerWorkReqDto;
import com.glanner.api.dto.request.AddUserToGlannerReqDto;
import com.glanner.api.dto.request.ChangeGlannerNameReqDto;
import com.glanner.api.dto.request.UpdateGlannerWorkReqDto;
import com.glanner.api.dto.response.FindAttendedGlannerResDto;
import com.glanner.api.dto.response.FindGlannerResDto;

import java.util.List;

public interface GlannerService {

    Long saveGlanner(String hostEmail);
    void deleteGlanner(Long id);
    void changeGlannerName(ChangeGlannerNameReqDto reqDto);
    List<FindAttendedGlannerResDto> findAttendedGlanners(String userEmail);
    FindGlannerResDto findGlannerDetail(Long id);
    void addUser(AddUserToGlannerReqDto reqDto);
    void deleteUser(Long glannerId, Long userId);
    void addDailyWork(AddGlannerWorkReqDto reqDto);
    void deleteDailyWork(Long glannerId, Long workId);
    void updateDailyWork(UpdateGlannerWorkReqDto reqDto);

}
