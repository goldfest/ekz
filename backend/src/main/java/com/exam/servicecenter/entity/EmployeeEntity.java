package com.exam.servicecenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String fullName;

    @Column(nullable = false, length = 120)
    private String position;

    @Column(length = 40)
    private String phone;

    @Column(length = 120)
    private String email;

    @Column(nullable = false)
    private boolean active;
}
