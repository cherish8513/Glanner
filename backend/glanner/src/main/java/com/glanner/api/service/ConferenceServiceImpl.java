package com.glanner.api.service;

import com.glanner.api.dto.request.AddParticipantReqDto;
import com.glanner.api.dto.request.OpenConferenceReqDto;
import com.glanner.api.dto.response.FindConferenceResDto;
import com.glanner.api.exception.ConferenceNotFoundException;
import com.glanner.api.exception.GlannerNotFoundException;
import com.glanner.api.exception.UserNotFoundException;
import com.glanner.core.domain.glanner.Glanner;
import com.glanner.core.domain.user.Conference;
import com.glanner.core.domain.user.User;
import com.glanner.core.domain.user.UserConference;
import com.glanner.core.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConferenceServiceImpl implements ConferenceService {

    private final UserRepository userRepository;
    private final GlannerRepository glannerRepository;
    private final ConferenceRepository conferenceRepository;
    private final UserConferenceRepository userConferenceRepository;
    private final UserConferenceCustomRepository userConferenceCustomRepository;

    @Override
    public void openConference(String hostEmail, OpenConferenceReqDto reqDto) {
        User findUser = userRepository.findByEmail(hostEmail).orElseThrow(UserNotFoundException::new);
        Glanner findGlanner = glannerRepository.findById(reqDto.getGlannerId()).orElseThrow(GlannerNotFoundException::new);

        Conference conference = reqDto.toEntity(findUser, findGlanner);

        Conference savedConference = conferenceRepository.save(conference);

        UserConference userConference = UserConference.builder()
                .user(findUser)
                .conference(savedConference)
                .build();

        userConferenceRepository.save(userConference);
    }

    @Override
    public void closeConference(Long id) {
        Conference conference = conferenceRepository.findById(id).orElseThrow(ConferenceNotFoundException::new);
        conference.changeEndTime(LocalDateTime.now());
        conference.changeStatus();
    }

    @Override
    public void addParticipant(AddParticipantReqDto reqDto) {
        User user = userRepository.findById(reqDto.getUserId()).orElseThrow(UserNotFoundException::new);
        Conference conference = conferenceRepository.findById(reqDto.getConferenceId()).orElseThrow(ConferenceNotFoundException::new);
        UserConference userConference = UserConference.builder()
                .user(user)
                .conference(conference)
                .build();
        userConferenceRepository.save(userConference);
    }

    @Override
    public FindConferenceResDto findConferenceDetail(Long id) {
        Conference conference = conferenceRepository.findById(id).orElseThrow(ConferenceNotFoundException::new);
        List<UserConference> userConferences = userConferenceCustomRepository.findByConferenceId(id);

        return new FindConferenceResDto(conference, userConferences);
    }


}
