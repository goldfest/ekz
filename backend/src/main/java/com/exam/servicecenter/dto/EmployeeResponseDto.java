package com.exam.servicecenter.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDto {
    private Long id;
    private String fullName;
    private String position;
    private String phone;
    private String email;
    private boolean active;
}