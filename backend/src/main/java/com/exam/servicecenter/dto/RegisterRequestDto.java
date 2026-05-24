package com.exam.servicecenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    @NotBlank(message = "Логин обязателен")
    @Size(min = 3, max = 80, message = "Логин должен быть от 3 до 80 символов")
    private String username;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, max = 100, message = "Пароль должен быть от 6 символов")
    private String password;
}
