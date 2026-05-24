package com.exam.servicecenter.controller;

import com.exam.servicecenter.dto.RoleUpdateRequest;
import com.exam.servicecenter.dto.UserResponseDto;
import com.exam.servicecenter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public Page<UserResponseDto> findAll(@RequestParam(required = false) String search, Pageable pageable) {
        return userService.findAll(search, pageable);
    }

    @PatchMapping("/{id}/role")
    public UserResponseDto updateRole(@PathVariable Long id, @Valid @RequestBody RoleUpdateRequest request) {
        return userService.updateRole(id, request.getRole());
    }

    @PatchMapping("/{id}/enabled")
    public UserResponseDto setEnabled(@PathVariable Long id, @RequestParam boolean enabled) {
        return userService.setEnabled(id, enabled);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
