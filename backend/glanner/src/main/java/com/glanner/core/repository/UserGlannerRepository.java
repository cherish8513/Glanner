package com.glanner.core.repository;

import com.glanner.core.domain.glanner.UserGlanner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGlannerRepository extends JpaRepository<UserGlanner, Long>, UserGlannerCustomRepository {
}