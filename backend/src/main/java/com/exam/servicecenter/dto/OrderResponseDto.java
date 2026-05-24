package com.exam.servicecenter.dto;

import com.exam.servicecenter.enums.OrderStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String title;
    private String description;
    private OrderStatus status;
    private LocalDate acceptedAt;
    private LocalDate dueDate;
    private Long clientId;
    private String clientName;
    private Long employeeId;
    private String employeeName;
    private List<Long> serviceIds;
    private List<String> serviceTitles;
}
