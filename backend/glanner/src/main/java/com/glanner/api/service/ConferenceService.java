package com.glanner.api.service;

import com.glanner.api.dto.request.AddParticipantReqDto;
import com.glanner.api.dto.request.OpenConferenceReqDto;
import com.glanner.api.dto.response.FindConferenceResDto;

public interface ConferenceService {
    void openConference(String hostEmail, OpenConferenceReqDto reqDto);
    void closeConference(Long id);
    void addParticipant(AddParticipantReqDto reqDto);
    FindConferenceResDto findConferenceDetail(Long id);
}
