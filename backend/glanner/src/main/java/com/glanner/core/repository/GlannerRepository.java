package com.glanner.core.repository;

import com.glanner.core.domain.glanner.Glanner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlannerRepository extends JpaRepository<Glanner, Long>, GlannerCustomRepository{
}