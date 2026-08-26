package com.trekmate.dto;

import com.trekmate.entity.Difficulty;

public record TrekSearchCriteria(String keyword, String state, Difficulty difficulty, String season,
                                 Integer minDurationDays, Integer maxDurationDays) {
}
