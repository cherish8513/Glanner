package com.glanner.core.repository;

import com.glanner.core.domain.user.UserConference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConferenceRepository extends JpaRepository<UserConference, Long> {
}
