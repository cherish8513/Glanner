package com.glanner.core.repository;

import com.glanner.core.domain.glanner.UserGlanner;

import java.util.List;

public interface UserGlannerCustomRepository {
    List<UserGlanner> findByGlannerId(Long glannerId);
    UserGlanner findByUserIdAndGlannerId(Long userId, Long glannerId);
}
