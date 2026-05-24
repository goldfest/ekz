package com.exam.servicecenter.service;

import com.exam.servicecenter.dto.OrderRequestDto;
import com.exam.servicecenter.dto.OrderResponseDto;
import com.exam.servicecenter.entity.ClientEntity;
import com.exam.servicecenter.entity.EmployeeEntity;
import com.exam.servicecenter.entity.OrderEntity;
import com.exam.servicecenter.entity.ServiceEntity;
import com.exam.servicecenter.enums.OrderStatus;
import com.exam.servicecenter.exception.NotFoundException;
import com.exam.servicecenter.repository.ClientRepository;
import com.exam.servicecenter.repository.EmployeeRepository;
import com.exam.servicecenter.repository.OrderRepository;
import com.exam.servicecenter.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> findAll(String search,
                                          OrderStatus status,
                                          Long clientId,
                                          Long employeeId,
                                          Long serviceId,
                                          Pageable pageable) {
        return orderRepository.findFiltered(search, status, clientId, employeeId, serviceId, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findById(Long id) {
        return toDto(getOrder(id));
    }

    @Transactional
    public OrderResponseDto create(OrderRequestDto dto) {
        ClientEntity client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new NotFoundException("Клиент с id " + dto.getClientId() + " не найден"));

        EmployeeEntity employee = resolveEmployee(dto.getEmployeeId());
        Set<ServiceEntity> services = resolveServices(dto.getServiceIds());

        OrderEntity order = OrderEntity.builder()
                .title(dto.getTitle().trim())
                .description(dto.getDescription())
                .status(dto.getStatus() == null ? OrderStatus.NEW : dto.getStatus())
                .acceptedAt(LocalDate.now())
                .dueDate(dto.getDueDate())
                .client(client)
                .employee(employee)
                .services(services)
                .build();

        return toDto(orderRepository.save(order));
    }

    @Transactional
    public OrderResponseDto update(Long id, OrderRequestDto dto) {
        OrderEntity order = getOrder(id);

        order.setTitle(dto.getTitle().trim());
        order.setDescription(dto.getDescription());
        order.setStatus(dto.getStatus() == null ? order.getStatus() : dto.getStatus());
        order.setDueDate(dto.getDueDate());
        order.setClient(clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new NotFoundException("Клиент с id " + dto.getClientId() + " не найден")));
        order.setEmployee(resolveEmployee(dto.getEmployeeId()));
        order.setServices(resolveServices(dto.getServiceIds()));

        return toDto(orderRepository.save(order));
    }

    @Transactional
    public void delete(Long id) {
        orderRepository.delete(getOrder(id));
    }

    private OrderEntity getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ с id " + id + " не найден"));
    }

    private EmployeeEntity resolveEmployee(Long employeeId) {
        if (employeeId == null) {
            return null;
        }

        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NotFoundException("Сотрудник с id " + employeeId + " не найден"));
    }

    private Set<ServiceEntity> resolveServices(Set<Long> serviceIds) {
        if (serviceIds == null || serviceIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<Long> requestedIds = serviceIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (requestedIds.isEmpty()) {
            return new HashSet<>();
        }

        List<ServiceEntity> services = serviceRepository.findAllById(requestedIds);

        Set<Long> foundIds = services.stream()
                .map(ServiceEntity::getId)
                .collect(Collectors.toSet());

        Set<Long> notFoundIds = requestedIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toCollection(TreeSet::new));

        if (!notFoundIds.isEmpty()) {
            throw new NotFoundException("Услуги с id " + notFoundIds + " не найдены");
        }

        return new HashSet<>(services);
    }

    private OrderResponseDto toDto(OrderEntity order) {
        List<Long> serviceIds = order.getServices().stream()
                .map(ServiceEntity::getId)
                .sorted()
                .toList();

        List<String> serviceTitles = order.getServices().stream()
                .map(ServiceEntity::getTitle)
                .sorted()
                .toList();

        return OrderResponseDto.builder()
                .id(order.getId())
                .title(order.getTitle())
                .description(order.getDescription())
                .status(order.getStatus())
                .acceptedAt(order.getAcceptedAt())
                .dueDate(order.getDueDate())
                .clientId(order.getClient().getId())
                .clientName(order.getClient().getFullName())
                .employeeId(order.getEmployee() == null ? null : order.getEmployee().getId())
                .employeeName(order.getEmployee() == null ? null : order.getEmployee().getFullName())
                .serviceIds(serviceIds)
                .serviceTitles(serviceTitles)
                .build();
    }
}