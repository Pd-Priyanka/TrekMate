package com.trekmate.dto;

import java.time.Instant;

public record ReviewResponse(Long id, Integer rating, String comment, Instant createdAt, Long userId, String userName) {
}
