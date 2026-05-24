package com.exam.servicecenter.controller;

import com.exam.servicecenter.dto.ServiceRequestDto;
import com.exam.servicecenter.entity.ServiceEntity;
import com.exam.servicecenter.service.ServiceCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceCatalogService serviceCatalogService;

    @GetMapping
    public Page<ServiceEntity> findAll(@RequestParam(required = false) String search,
                                       @RequestParam(required = false) String category,
                                       @RequestParam(required = false) Boolean active,
                                       Pageable pageable) {
        return serviceCatalogService.findAll(search, category, active, pageable);
    }

    @GetMapping("/{id}")
    public ServiceEntity findById(@PathVariable Long id) {
        return serviceCatalogService.findById(id);
    }

    @PostMapping
    public ServiceEntity create(@Valid @RequestBody ServiceRequestDto dto) {
        return serviceCatalogService.create(dto);
    }

    @PutMapping("/{id}")
    public ServiceEntity update(@PathVariable Long id, @Valid @RequestBody ServiceRequestDto dto) {
        return serviceCatalogService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        serviceCatalogService.delete(id);
    }
}
