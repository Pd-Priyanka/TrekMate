package com.trekmate.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.trekmate.entity.Difficulty;

public record TrekResponse(Long id, String name, String slug, String location, String state, String country,
                           Difficulty difficulty, BigDecimal distanceKm, Integer durationDays, Integer altitudeMeters,
                           String bestSeason, String description, String imageUrl, BigDecimal latitude,
                           BigDecimal longitude, Instant createdAt) {
}
