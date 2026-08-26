package com.trekmate.dto;

import java.math.BigDecimal;

import com.trekmate.entity.Difficulty;
import jakarta.validation.constraints.*;

public record TrekRequest(
        @NotBlank @Size(max = 150) String name,
        @NotBlank @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "must be a lowercase, hyphen-separated value") @Size(max = 180) String slug,
        @NotBlank @Size(max = 150) String location,
        @NotBlank @Size(max = 100) String state,
        @NotBlank @Size(max = 100) String country,
        @NotNull Difficulty difficulty,
        @NotNull @DecimalMin(value = "0.1") @Digits(integer = 6, fraction = 2) BigDecimal distanceKm,
        @NotNull @Min(1) @Max(365) Integer durationDays,
        @NotNull @Min(1) @Max(10000) Integer altitudeMeters,
        @NotBlank @Size(max = 100) String bestSeason,
        @NotBlank @Size(max = 10000) String description,
        @Size(max = 500) String imageUrl,
        @NotNull @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0") @Digits(integer = 2, fraction = 7) BigDecimal latitude,
        @NotNull @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0") @Digits(integer = 3, fraction = 7) BigDecimal longitude) {
}
