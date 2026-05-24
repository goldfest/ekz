package com.exam.servicecenter.dto;

import com.exam.servicecenter.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotBlank(message = "Название заказа обязательно")
    @Size(max = 160)
    private String title;

    @Size(max = 1000)
    private String description;

    private OrderStatus status;

    @NotNull(message = "Клиент обязателен")
    private Long clientId;

    private Long employeeId;

    private LocalDate dueDate;

    private Set<Long> serviceIds;
}
