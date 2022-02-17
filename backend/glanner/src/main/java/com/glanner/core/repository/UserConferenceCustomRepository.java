package com.glanner.core.repository;

import com.glanner.core.domain.user.UserConference;

import java.util.List;

public interface UserConferenceCustomRepository{
    List<UserConference> findByConferenceId(Long conferenceId);
}
