package com.exam.servicecenter.controller;

import com.exam.servicecenter.dto.EmployeeRequestDto;
import com.exam.servicecenter.dto.EmployeeResponseDto;
import com.exam.servicecenter.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public Page<EmployeeResponseDto> findAll(@RequestParam(required = false) String search,
                                             @RequestParam(required = false) Boolean active,
                                             @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return employeeService.findAll(search, active, pageable);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDto findById(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PostMapping
    public EmployeeResponseDto create(@Valid @RequestBody EmployeeRequestDto dto) {
        return employeeService.create(dto);
    }

    @PutMapping("/{id}")
    public EmployeeResponseDto update(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDto dto) {
        return employeeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }
}