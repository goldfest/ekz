package com.exam.servicecenter.dto;

import com.exam.servicecenter.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private Role role;
    private boolean enabled;
}
