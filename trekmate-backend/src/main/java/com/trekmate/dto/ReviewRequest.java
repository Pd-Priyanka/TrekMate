package com.trekmate.dto;

import jakarta.validation.constraints.*;

public record ReviewRequest(@Min(1) @Max(5) Integer rating, @NotBlank @Size(max = 2000) String comment) {
}
