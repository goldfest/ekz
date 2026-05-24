package com.exam.servicecenter.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDto {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private boolean active;
    private LocalDate registeredAt;
}