package com.trekmate.dto;

import java.time.Instant;

import com.trekmate.entity.Role;

public record UserResponse(Long id, String name, String email, Role role, Instant createdAt) {
}
