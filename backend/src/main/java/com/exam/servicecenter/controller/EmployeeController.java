package com.exam.servicecenter.controller;

import com.exam.servicecenter.dto.EmployeeRequestDto;
import com.exam.servicecenter.entity.EmployeeEntity;
import com.exam.servicecenter.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public Page<EmployeeEntity> findAll(@RequestParam(required = false) String search,
                                        @RequestParam(required = false) Boolean active,
                                        Pageable pageable) {
        return employeeService.findAll(search, active, pageable);
    }

    @GetMapping("/{id}")
    public EmployeeEntity findById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PostMapping
    public EmployeeEntity create(@Valid @RequestBody EmployeeRequestDto dto) {
        return employeeService.create(dto);
    }

    @PutMapping("/{id}")
    public EmployeeEntity update(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDto dto) {
        return employeeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
