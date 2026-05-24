package com.exam.servicecenter.dto;

import com.exam.servicecenter.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String username;
    private Role role;
}
