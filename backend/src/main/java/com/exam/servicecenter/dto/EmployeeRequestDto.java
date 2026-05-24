package com.exam.servicecenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {
    @NotBlank(message = "ФИО сотрудника обязательно")
    @Size(max = 160)
    private String fullName;

    @NotBlank(message = "Должность обязательна")
    @Size(max = 120)
    private String position;

    @Size(max = 40)
    private String phone;

    @Size(max = 120)
    private String email;

    private Boolean active;
}
