package com.glanner.api.dto.response;

import com.glanner.core.domain.user.Conference;
import com.glanner.core.domain.user.ConferenceActiveStatus;
import com.glanner.core.domain.user.UserConference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
public class FindConferenceResDto {
    public FindConferenceResDto(Conference conference, List<UserConference> userConferences) {
        this.conferenceId = conference.getId();
        this.ownerId = conference.getOwner().getId();
        this.callStartTime = conference.getCallStartTime();
        this.callEndTime = conference.getCallEndTime();
        this.attendLimit = conference.getAttendLimit();
        this.attendeeNumber = userConferences.size();
        this.isActivate = conference.getIsActive();
    }
    Long conferenceId;
    Long ownerId;
    LocalDateTime callStartTime;
    LocalDateTime callEndTime;
    int attendLimit;
    ConferenceActiveStatus isActivate;
    int attendeeNumber;
}
