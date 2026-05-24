package com.exam.servicecenter.service;

import com.exam.servicecenter.dto.AuthResponseDto;
import com.exam.servicecenter.dto.LoginRequestDto;
import com.exam.servicecenter.dto.RegisterRequestDto;
import com.exam.servicecenter.entity.UserEntity;
import com.exam.servicecenter.enums.Role;
import com.exam.servicecenter.repository.UserRepository;
import com.exam.servicecenter.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        UserEntity saved = userRepository.save(user);
        return buildAuthResponse(saved);
    }

    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        return buildAuthResponse(user);
    }

    private AuthResponseDto buildAuthResponse(UserEntity user) {
        return AuthResponseDto.builder()
                .token(jwtService.generateToken(user))
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
