package com.exam.servicecenter.entity;

import com.exam.servicecenter.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDate acceptedAt;

    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_services",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<ServiceEntity> services = new HashSet<>();
}
