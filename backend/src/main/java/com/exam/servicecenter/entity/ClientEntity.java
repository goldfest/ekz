package com.exam.servicecenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String fullName;

    @Column(nullable = false, length = 40)
    private String phone;

    @Column(length = 120)
    private String email;

    @Column(length = 250)
    private String address;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDate registeredAt;
}
