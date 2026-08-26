package com.trekmate.service;

import com.trekmate.dto.AuthResponse;
import com.trekmate.dto.LoginRequest;
import com.trekmate.dto.RegisterRequest;
import com.trekmate.dto.UserResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser(String email);
}
