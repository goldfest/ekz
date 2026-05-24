package com.exam.servicecenter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Логин обязателен")
    private String username;

    @NotBlank(message = "Пароль обязателен")
    private String password;
}
