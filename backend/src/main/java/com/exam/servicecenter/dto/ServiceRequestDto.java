package com.exam.servicecenter.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestDto {
    @NotBlank(message = "Название услуги обязательно")
    @Size(max = 160)
    private String title;

    @NotBlank(message = "Категория обязательна")
    @Size(max = 100)
    private String category;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;

    private Boolean active;
}
