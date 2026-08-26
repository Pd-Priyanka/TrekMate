package com.trekmate.dto;

import java.time.Instant;

public record FavoriteResponse(Long id, TrekResponse trek, Instant createdAt) {
}
