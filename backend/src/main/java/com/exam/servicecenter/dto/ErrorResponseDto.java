package com.exam.servicecenter.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Map<String, String> errors;
}
