package com.trekmate.mapper;

import org.springframework.stereotype.Component;
import com.trekmate.dto.UserResponse;
import com.trekmate.entity.User;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCreatedAt());
    }
}
