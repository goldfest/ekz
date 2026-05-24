package com.exam.servicecenter.service;

import com.exam.servicecenter.dto.EmployeeRequestDto;
import com.exam.servicecenter.entity.EmployeeEntity;
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
    public Page<EmployeeEntity> findAll(String search, Boolean active, Pageable pageable) {
        return employeeRepository.findFiltered(search, active, pageable);
    }

    @Transactional(readOnly = true)
    public EmployeeEntity findById(Long id) {
        return getEmployee(id);
    }

    @Transactional
    public EmployeeEntity create(EmployeeRequestDto dto) {
        EmployeeEntity employee = EmployeeEntity.builder()
                .fullName(dto.getFullName().trim())
                .position(dto.getPosition().trim())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .active(dto.getActive() == null || dto.getActive())
                .build();
        return employeeRepository.save(employee);
    }

    @Transactional
    public EmployeeEntity update(Long id, EmployeeRequestDto dto) {
        EmployeeEntity employee = getEmployee(id);
        employee.setFullName(dto.getFullName().trim());
        employee.setPosition(dto.getPosition().trim());
        employee.setPhone(dto.getPhone());
        employee.setEmail(dto.getEmail());
        employee.setActive(dto.getActive() == null || dto.getActive());
        return employeeRepository.save(employee);
    }

    @Transactional
    public void delete(Long id) {
        employeeRepository.delete(getEmployee(id));
    }

    private EmployeeEntity getEmployee(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник с id " + id + " не найден"));
    }
}
