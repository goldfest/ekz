package com.exam.servicecenter.service;

import com.exam.servicecenter.dto.ServiceRequestDto;
import com.exam.servicecenter.dto.ServiceResponseDto;
import com.exam.servicecenter.entity.ServiceEntity;
import com.exam.servicecenter.exception.NotFoundException;
import com.exam.servicecenter.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceCatalogService {
    private final ServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    public Page<ServiceResponseDto> findAll(String search, String category, Boolean active, Pageable pageable) {
        return serviceRepository.findFiltered(search, category, active, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public ServiceResponseDto findById(Long id) {
        return toDto(getService(id));
    }

    @Transactional
    public ServiceResponseDto create(ServiceRequestDto dto) {
        ServiceEntity service = ServiceEntity.builder()
                .title(dto.getTitle().trim())
                .category(dto.getCategory().trim())
                .price(dto.getPrice())
                .active(dto.getActive() == null || dto.getActive())
                .build();

        return toDto(serviceRepository.save(service));
    }

    @Transactional
    public ServiceResponseDto update(Long id, ServiceRequestDto dto) {
        ServiceEntity service = getService(id);

        service.setTitle(dto.getTitle().trim());
        service.setCategory(dto.getCategory().trim());
        service.setPrice(dto.getPrice());

        if (dto.getActive() != null) {
            service.setActive(dto.getActive());
        }

        return toDto(serviceRepository.save(service));
    }

    @Transactional
    public void delete(Long id) {
        ServiceEntity service = getService(id);
        service.setActive(false);
        serviceRepository.save(service);
    }

    private ServiceEntity getService(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Услуга с id " + id + " не найдена"));
    }

    private ServiceResponseDto toDto(ServiceEntity service) {
        return ServiceResponseDto.builder()
                .id(service.getId())
                .title(service.getTitle())
                .category(service.getCategory())
                .price(service.getPrice())
                .active(service.isActive())
                .build();
    }
}