package com.exam.servicecenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDto {
    @NotBlank(message = "ФИО клиента обязательно")
    @Size(max = 160)
    private String fullName;

    @NotBlank(message = "Телефон обязателен")
    @Size(max = 40)
    private String phone;

    @Size(max = 120)
    private String email;

    @Size(max = 250)
    private String address;

    private Boolean active;
}
