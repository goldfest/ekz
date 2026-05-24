package com.exam.servicecenter.dto;

import com.exam.servicecenter.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest {
    @NotNull
    private Role role;
}
