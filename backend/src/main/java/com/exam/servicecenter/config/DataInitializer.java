package com.exam.servicecenter.config;

import com.exam.servicecenter.entity.ClientEntity;
import com.exam.servicecenter.entity.EmployeeEntity;
import com.exam.servicecenter.entity.OrderEntity;
import com.exam.servicecenter.entity.ServiceEntity;
import com.exam.servicecenter.entity.UserEntity;
import com.exam.servicecenter.enums.OrderStatus;
import com.exam.servicecenter.enums.Role;
import com.exam.servicecenter.repository.ClientRepository;
import com.exam.servicecenter.repository.EmployeeRepository;
import com.exam.servicecenter.repository.OrderRepository;
import com.exam.servicecenter.repository.ServiceRepository;
import com.exam.servicecenter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        createUsers();
        createDemoData();
    }

    private void createUsers() {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(UserEntity.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build());
        }
        if (!userRepository.existsByUsername("user")) {
            userRepository.save(UserEntity.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.USER)
                    .enabled(true)
                    .build());
        }
    }

    private void createDemoData() {
        if (clientRepository.count() > 0) {
            return;
        }

        ClientEntity client1 = clientRepository.save(ClientEntity.builder()
                .fullName("Иван Петров")
                .phone("+7 900 111-22-33")
                .email("ivan@example.com")
                .address("ул. Центральная, 10")
                .active(true)
                .registeredAt(LocalDate.now().minusDays(7))
                .build());
        ClientEntity client2 = clientRepository.save(ClientEntity.builder()
                .fullName("Анна Смирнова")
                .phone("+7 900 222-33-44")
                .email("anna@example.com")
                .address("пр. Мира, 5")
                .active(true)
                .registeredAt(LocalDate.now().minusDays(3))
                .build());

        EmployeeEntity master = employeeRepository.save(EmployeeEntity.builder()
                .fullName("Сергей Иванов")
                .position("Мастер по ремонту")
                .phone("+7 900 333-44-55")
                .email("master@example.com")
                .active(true)
                .build());
        EmployeeEntity manager = employeeRepository.save(EmployeeEntity.builder()
                .fullName("Ольга Кузнецова")
                .position("Менеджер")
                .phone("+7 900 444-55-66")
                .email("manager@example.com")
                .active(true)
                .build());

        ServiceEntity diagnostics = serviceRepository.save(ServiceEntity.builder()
                .title("Диагностика устройства")
                .category("Диагностика")
                .price(new BigDecimal("800.00"))
                .active(true)
                .build());
        ServiceEntity repair = serviceRepository.save(ServiceEntity.builder()
                .title("Ремонт техники")
                .category("Ремонт")
                .price(new BigDecimal("2500.00"))
                .active(true)
                .build());
        ServiceEntity setup = serviceRepository.save(ServiceEntity.builder()
                .title("Настройка ПО")
                .category("Программное обеспечение")
                .price(new BigDecimal("1200.00"))
                .active(true)
                .build());

        orderRepository.save(OrderEntity.builder()
                .title("Ремонт ноутбука")
                .description("Не включается после обновления")
                .status(OrderStatus.IN_PROGRESS)
                .acceptedAt(LocalDate.now().minusDays(2))
                .dueDate(LocalDate.now().plusDays(3))
                .client(client1)
                .employee(master)
                .services(Set.of(diagnostics, repair))
                .build());
        orderRepository.save(OrderEntity.builder()
                .title("Настройка рабочего ПК")
                .description("Установка программ и настройка системы")
                .status(OrderStatus.NEW)
                .acceptedAt(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(1))
                .client(client2)
                .employee(manager)
                .services(Set.of(diagnostics, setup))
                .build());
    }
}
