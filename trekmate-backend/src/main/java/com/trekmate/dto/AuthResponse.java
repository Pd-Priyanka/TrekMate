package com.trekmate.dto;

public record AuthResponse(String accessToken, String tokenType, UserResponse user) {
}
