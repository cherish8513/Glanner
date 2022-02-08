package com.glanner.core.repository;

import com.glanner.core.domain.user.DailyWorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyWorkScheduleRepository extends JpaRepository<DailyWorkSchedule, Long> {
}