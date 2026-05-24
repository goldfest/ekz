package com.exam.servicecenter.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponseDto {
    private Long id;
    private String title;
    private String category;
    private BigDecimal price;
    private boolean active;
}