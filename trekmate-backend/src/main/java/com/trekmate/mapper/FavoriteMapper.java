package com.trekmate.mapper;

import org.springframework.stereotype.Component;
import com.trekmate.dto.FavoriteResponse;
import com.trekmate.entity.Favorite;

@Component
public class FavoriteMapper {
    private final TrekMapper trekMapper;

    public FavoriteMapper(TrekMapper trekMapper) {
        this.trekMapper = trekMapper;
    }

    public FavoriteResponse toResponse(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), trekMapper.toResponse(favorite.getTrek()), favorite.getCreatedAt());
    }
}
