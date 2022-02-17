package com.glanner.api.controller;

import com.glanner.api.dto.request.AddParticipantReqDto;
import com.glanner.api.dto.request.OpenConferenceReqDto;
import com.glanner.api.dto.response.BaseResponseEntity;
import com.glanner.api.dto.response.FindConferenceResDto;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.api.service.ConferenceService;
import com.glanner.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/conference")
@RequiredArgsConstructor
public class ConferenceController {
    private final ConferenceService conferenceService;

    @PostMapping
    public ResponseEntity<BaseResponseEntity> openConference(@RequestBody @Valid OpenConferenceReqDto reqDto){
        String hostEmail = getUsername(SecurityUtils.getCurrentUsername());
        conferenceService.openConference(hostEmail, reqDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseEntity> closeConference(@PathVariable Long id) {
        conferenceService.closeConference(id);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @PostMapping("/attendee")
    public ResponseEntity<BaseResponseEntity> addParticipant(@RequestBody @Valid AddParticipantReqDto reqDto) {
        conferenceService.addParticipant(reqDto);
        return ResponseEntity.status(200).body(new BaseResponseEntity(200, "Success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FindConferenceResDto> findConferenceDetail(@PathVariable Long id) {
        FindConferenceResDto resDto = conferenceService.findConferenceDetail(id);
        return ResponseEntity.status(200).body(resDto);
    }

    public String getUsername(Optional<String> username){
        return username.orElseThrow(UserNotFoundException::new);
    }
}
