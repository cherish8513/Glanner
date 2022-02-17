package com.glanner.core.repository;

import com.glanner.core.domain.glanner.DailyWorkGlanner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyWorkGlannerRepository extends JpaRepository<DailyWorkGlanner, Long> {
}