package com.trekmate.service;

import com.trekmate.dto.FavoriteCountResponse;
import com.trekmate.dto.FavoriteListResponse;
import com.trekmate.dto.FavoriteMutationResponse;

public interface FavoriteService {
    FavoriteMutationResponse add(String email, Long trekId);

    FavoriteCountResponse remove(String email, Long trekId);

    FavoriteListResponse findAll(String email);
}
