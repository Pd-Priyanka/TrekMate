package com.trekmate.dto;

import java.util.List;

public record FavoriteListResponse(long favoriteCount, List<FavoriteResponse> favorites) {
}
