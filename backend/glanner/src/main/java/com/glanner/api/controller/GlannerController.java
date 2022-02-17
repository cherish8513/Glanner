package com.glanner.api.controller;

import com.glanner.api.dto.request.AddGlannerWorkReqDto;
import com.glanner.api.dto.request.AddUserToGlannerReqDto;
import com.glanner.api.dto.request.ChangeGlannerNameReqDto;
import com.glanner.api.dto.request.UpdateGlannerWorkReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindAttendedGlannerResDto;
import com.glanner.api.dto.response.FindGlannerResDto;
import com.glanner.api.dto.response.FindGlannerWorkResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.queryrepository.DailyWorkGlannerQueryRepository;
import com.glanner.api.queryrepository.GlannerQueryRepository;
import com.glanner.api.service.GlannerService;
import com.glanner.security.SecurityUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 그룹 플래너를 Glanner 라고 부르며, 가능한 모든 기능을 포함하는 컨트롤러.
 */
@RestController
@RequestMapping("/api/glanner")
@RequiredArgsConstructor
public class GlannerController {
    private final GlannerService glannerService;
    private final DailyWorkGlannerQueryRepository dailyWorkQueryRepository;
    private final GlannerQueryRepository glannerQueryRepository;

    @PostMapping
    @ApiOperation(value = "글래너 저장")
    public ResponseEntity<BaseResponseEntity> createGlanner(){
        String hostEmail = getUsername(SecurityUtils.getCurrentUsername());
        glannerService.saveGlanner(hostEmail);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "글래너 삭제")
    public ResponseEntity<BaseResponseEntity> deleteGlanner(@PathVariable Long id){
        glannerService.deleteGlanner(id);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }
    @PutMapping
    @ApiOperation(value = "글래너 이름 변경")
    public ResponseEntity<BaseResponseEntity> changeGlannerName(@RequestBody @Valid ChangeGlannerNameReqDto reqDto){
        glannerService.changeGlannerName(reqDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }
    @GetMapping
    @ApiOperation(value = "글래너 리스트 가져오기", notes = "로그인한 회원이 속해있는 모든 글래너들을 가져온다.")
    public ResponseEntity<List<FindAttendedGlannerResDto>> findGlannerList(){
        String userEmail = getUsername(SecurityUtils.getCurrentUsername());
        List<FindAttendedGlannerResDto> responseDto = glannerService.findAttendedGlanners(userEmail);
        return ResponseEntity.status(200).body(responseDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "특정 글래너 가져오기", notes = "글래너에 속한 모든 회원의 이름, 인원을 가져온다")
    public ResponseEntity<FindGlannerResDto> findGlannerDetail(@PathVariable Long id){
        FindGlannerResDto responseDto = glannerService.findGlannerDetail(id);
        return ResponseEntity.status(200).body(responseDto);
    }

    /**
     *
     * @param id : 가져올 일정이 속해있는 글래너의 Id
     * @param date : yyyy-mm-dd로 각 달의 시작 일을 인수로 받는다 ex) 2022.01.01
     * @return : 해당 달의 모든 일정 정보를 List Dto로 반환한다.
     */
    @GetMapping("/{id}/{date}")
    @ApiOperation(value = "특정 글래너의 일정 가져오기", notes = "'yyyy-mm-01'의 양식으로 mm + 1의 모든 일정을 가져온다. ex) 2022-02-01 ~ 2022-03-01")
    public ResponseEntity<List<FindGlannerWorkResDto>> findGlannerWorks(@PathVariable Long id, @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date){
        LocalDateTime dateTimeStart = date.atStartOfDay().minusMinutes(1);
        LocalDateTime dateTimeEnd = dateTimeStart.plusMonths(1);
        List<FindGlannerWorkResDto> responseDto = glannerQueryRepository.findDailyWorksDtoWithPeriod(id, dateTimeStart, dateTimeEnd);
        return ResponseEntity.status(200).body(responseDto);
    }

    @PostMapping("/user")
    @ApiOperation(value = "특정 글래너에 유저 추가")
    public ResponseEntity<BaseResponseEntity> addUser(@RequestBody @Valid AddUserToGlannerReqDto reqDto){
        glannerService.addUser(reqDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @DeleteMapping("/user/{glannerId}/{userId}")
    @ApiOperation(value = "특정 글래너의 참여 유저 삭제")
    public ResponseEntity<BaseResponseEntity> deleteUser(@PathVariable Long glannerId, @PathVariable Long userId){
        glannerService.deleteUser(glannerId, userId);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PostMapping("/work")
    @ApiOperation(value = "특정 글래너의 일정 추가")
    public ResponseEntity<BaseResponseEntity> addWork(@RequestBody @Valid AddGlannerWorkReqDto reqDto){
        glannerService.addDailyWork(reqDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @DeleteMapping("/work/{glannerId}/{workId}")
    @ApiOperation(value = "특정 글래너의 일정 삭제")
    public ResponseEntity<BaseResponseEntity> deleteWork(@PathVariable Long glannerId, @PathVariable Long workId){
        glannerService.deleteDailyWork(glannerId, workId);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PutMapping("/work")
    @ApiOperation(value = "특정 글래너의 일정 변경")
    public ResponseEntity<BaseResponseEntity> updateWork(@RequestBody @Valid UpdateGlannerWorkReqDto reqDto){
        glannerService.updateDailyWork(reqDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    /**
     *
     * @param glannerId : 일정이 속해있는 글래너 Id
     * @param startTime : 검색할 일정의 최소 일정 시간(언제 부터)
     * @param endTime : 검색할 일정의 최대 일정 시간 (언제 까지)
     * @return 찾아온 데이터의 Dto를 리스트로 반환
     */
    @ApiOperation(value = "특정 글래너의 일정 검색", notes = "일정 시작 시간이 yyyy-mm-dd'T'HH:mm의 양식으로 받은 두 시간 사이에 속하는 일정을 가져온다.")
    @GetMapping("/work/{glannerId}/{startTime}/{endTime}")
    public ResponseEntity<List<FindGlannerWorkResDto>> findWork(@PathVariable  Long glannerId,
                                                                @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") @ApiParam(value = "yyyy-MM-dd'T'HH:mm") LocalDateTime startTime,
                                                                @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm") @ApiParam(value = "yyyy-MM-dd'T'HH:mm") LocalDateTime endTime){

        List<FindGlannerWorkResDto> findGlannerWorkResDtos =
                dailyWorkQueryRepository.findByGlannerIdWithDate(glannerId, startTime, endTime);
        return ResponseEntity.status(200).body(findGlannerWorkResDtos);
    }

    public String getUsername(Optional<String> username){
        return username.orElseThrow(UserNotFoundException::new);
    }
}
