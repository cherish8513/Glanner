package com.glanner.core.repository;

import com.glanner.core.domain.user.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}