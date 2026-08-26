package com.trekmate.service;

import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.trekmate.dto.*;
import com.trekmate.entity.Role;
import com.trekmate.entity.User;
import com.trekmate.exception.ConflictException;
import com.trekmate.exception.NotFoundException;
import com.trekmate.exception.UnauthorizedException;
import com.trekmate.mapper.UserMapper;
import com.trekmate.repository.UserRepository;
import com.trekmate.security.JwtService;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (userRepository.existsByEmailIgnoreCase(email)) throw new ConflictException("Email is already registered.");
        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        User saved = userRepository.save(user);
        return response(saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.password()));
        } catch (BadCredentialsException exception) {
            throw new UnauthorizedException("Invalid email or password.");
        }
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UnauthorizedException("Invalid email or password."));
        return response(user);
    }

    @Override
    public UserResponse getCurrentUser(String email) {
        return userMapper.toResponse(userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new NotFoundException("User not found.")));
    }

    private AuthResponse response(User user) {
        return new AuthResponse(jwtService.generateToken(user), "Bearer", userMapper.toResponse(user));
    }
}
