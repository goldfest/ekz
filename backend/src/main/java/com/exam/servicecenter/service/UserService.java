package com.exam.servicecenter.service;

import com.exam.servicecenter.dto.UserResponseDto;
import com.exam.servicecenter.entity.UserEntity;
import com.exam.servicecenter.enums.Role;
import com.exam.servicecenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<UserResponseDto> findAll(String search, Pageable pageable) {
        return userRepository.findFiltered(search, pageable).map(this::toDto);
    }

    @Transactional
    public UserResponseDto updateRole(Long id, Role role) {
        UserEntity user = getUser(id);
        user.setRole(role);
        return toDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto setEnabled(Long id, boolean enabled) {
        UserEntity user = getUser(id);
        user.setEnabled(enabled);
        return toDto(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        UserEntity user = getUser(id);
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("Нельзя удалить текущего пользователя");
        }
        userRepository.delete(user);
    }

    private UserEntity getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с id " + id + " не найден"));
    }

    private UserResponseDto toDto(UserEntity user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .build();
    }
}
