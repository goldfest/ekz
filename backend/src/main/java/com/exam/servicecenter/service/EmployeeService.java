package com.exam.servicecenter.service;

import com.exam.servicecenter.dto.EmployeeRequestDto;
import com.exam.servicecenter.dto.EmployeeResponseDto;
import com.exam.servicecenter.entity.EmployeeEntity;
import com.exam.servicecenter.exception.NotFoundException;
import com.exam.servicecenter.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> findAll(String search, Boolean active, Pageable pageable) {
        return employeeRepository.findFiltered(search, active, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDto findById(Long id) {
        return toDto(getEmployee(id));
    }

    @Transactional
    public EmployeeResponseDto create(EmployeeRequestDto dto) {
        EmployeeEntity employee = EmployeeEntity.builder()
                .fullName(dto.getFullName().trim())
                .position(dto.getPosition().trim())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .active(dto.getActive() == null || dto.getActive())
                .build();

        return toDto(employeeRepository.save(employee));
    }

    @Transactional
    public EmployeeResponseDto update(Long id, EmployeeRequestDto dto) {
        EmployeeEntity employee = getEmployee(id);

        employee.setFullName(dto.getFullName().trim());
        employee.setPosition(dto.getPosition().trim());
        employee.setPhone(dto.getPhone());
        employee.setEmail(dto.getEmail());

        if (dto.getActive() != null) {
            employee.setActive(dto.getActive());
        }

        return toDto(employeeRepository.save(employee));
    }

    @Transactional
    public void delete(Long id) {
        EmployeeEntity employee = getEmployee(id);
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    private EmployeeEntity getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Сотрудник с id " + id + " не найден"));
    }

    private EmployeeResponseDto toDto(EmployeeEntity employee) {
        return EmployeeResponseDto.builder()
                .id(employee.getId())
                .fullName(employee.getFullName())
                .position(employee.getPosition())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .active(employee.isActive())
                .build();
    }
}