package com.glanner.api.controller;

import com.glanner.api.dto.request.AddPlannerWorkReqDto;
import com.glanner.api.dto.request.ChangePasswordReqDto;
import com.glanner.api.dto.request.SaveUserReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindPlannerWorkResDto;
import com.glanner.api.exception.DailyWorkNotFoundException;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.UserQueryRepository;
import com.glanner.api.service.UserService;
import com.glanner.core.domain.user.User;
import com.glanner.core.repository.DailyWorkScheduleRepository;
import com.glanner.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserQueryRepository userQueryRepository;
    private final DailyWorkScheduleRepository dailyWorkScheduleRepository;

    @PostMapping
    @ApiOperation(value = "회원 가입")
    public ResponseEntity<BaseResponseEntity> join(@RequestBody SaveUserReqDto requestDto) {
        userService.saveUser(requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @DeleteMapping
    @ApiOperation(value = "회원 탈퇴")
    public ResponseEntity<BaseResponseEntity> delete() {
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        userService.delete(userEmail);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PutMapping("/password")
    @ApiOperation(value = "비밀번호 변경")
    public ResponseEntity<BaseResponseEntity> changePassword(@RequestBody ChangePasswordReqDto requestDto) {
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        userService.changePassword(userEmail, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PostMapping("/planner/work")
    @ApiOperation(value = "개인 일정 추가")
    public ResponseEntity<BaseResponseEntity> addWork(@RequestBody AddPlannerWorkReqDto requestDto) {
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        userService.addWork(userEmail, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @GetMapping("/planner/work/{id}")
    @ApiOperation(value = "특정 개인 일정 상세정보 가져오기")
    public ResponseEntity<FindPlannerWorkResDto> getWork(@PathVariable Long id) {
        FindPlannerWorkResDto responseDto = userQueryRepository.findDailyWork(id).orElseThrow(DailyWorkNotFoundException::new);
        return ResponseEntity.status(200).body(responseDto);
    }

    @DeleteMapping("/planner/work/{id}")
    @ApiOperation(value = "특정 개인 일정 삭제")
    public ResponseEntity<BaseResponseEntity> deleteWork(@PathVariable Long id) {
        dailyWorkScheduleRepository.deleteById(id);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PutMapping("/planner/work/{id}")
    @ApiOperation(value = "특정 개인 일정 수정")
    public ResponseEntity<BaseResponseEntity> modifyWork(@PathVariable Long id, @RequestBody AddPlannerWorkReqDto requestDto) {
        userService.modifyWork(id, requestDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @GetMapping("/planner/{date}")
    @ApiOperation(value = "특정 개인 일정 가져오기", notes = "'yyyy-mm-01'의 양식으로 mm + 1의 모든 일정을 가져온다. ex) 2022-02-01 ~ 2022-03-01")
    public ResponseEntity<List<FindPlannerWorkResDto>> getWorks(@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date) {
        LocalDateTime dateTimeStart = date.atStartOfDay().minusMinutes(1);
        LocalDateTime dateTimeEnd = dateTimeStart.plusMonths(1);
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        List<FindPlannerWorkResDto> responseDto = userService.getWorks(userEmail, dateTimeStart, dateTimeEnd);
        return ResponseEntity.status(200).body(responseDto);
    }

    public String getUsername(Optional<String> username){
        return username.orElseThrow(UserNotFoundException::new);
    }
    public User getUser(Optional<User> user){
        return user.orElseThrow(UserNotFoundException::new);
    }
}