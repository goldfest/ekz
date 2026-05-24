package com.exam.servicecenter.controller;

import com.exam.servicecenter.dto.ServiceRequestDto;
import com.exam.servicecenter.dto.ServiceResponseDto;
import com.exam.servicecenter.service.ServiceCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceCatalogService serviceCatalogService;

    @GetMapping
    public Page<ServiceResponseDto> findAll(@RequestParam(required = false) String search,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) Boolean active,
                                            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return serviceCatalogService.findAll(search, category, active, pageable);
    }

    @GetMapping("/{id}")
    public ServiceResponseDto findById(@PathVariable Long id) {
        return serviceCatalogService.findById(id);
    }

    @PostMapping
    public ServiceResponseDto create(@Valid @RequestBody ServiceRequestDto dto) {
        return serviceCatalogService.create(dto);
    }

    @PutMapping("/{id}")
    public ServiceResponseDto update(@PathVariable Long id, @Valid @RequestBody ServiceRequestDto dto) {
        return serviceCatalogService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        serviceCatalogService.delete(id);
    }
}