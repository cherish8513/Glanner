package com.glanner.core.repository;

import com.glanner.core.domain.user.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}
