package com.exam.servicecenter.controller;

import com.exam.servicecenter.dto.AuthResponseDto;
import com.exam.servicecenter.dto.LoginRequestDto;
import com.exam.servicecenter.dto.RegisterRequestDto;
import com.exam.servicecenter.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponseDto register(@Valid @RequestBody RegisterRequestDto request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return authService.login(request);
    }
}
