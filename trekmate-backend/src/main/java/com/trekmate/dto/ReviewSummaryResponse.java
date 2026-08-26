package com.trekmate.dto;

import java.math.BigDecimal;
import java.util.List;

public record ReviewSummaryResponse(BigDecimal averageRating, long reviewCount, List<ReviewResponse> reviews) {
}
