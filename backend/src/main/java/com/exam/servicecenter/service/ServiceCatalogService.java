package com.exam.servicecenter.service;

import com.exam.servicecenter.dto.ServiceRequestDto;
import com.exam.servicecenter.entity.ServiceEntity;
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
    public Page<ServiceEntity> findAll(String search, String category, Boolean active, Pageable pageable) {
        return serviceRepository.findFiltered(search, category, active, pageable);
    }

    @Transactional(readOnly = true)
    public ServiceEntity findById(Long id) {
        return getService(id);
    }

    @Transactional
    public ServiceEntity create(ServiceRequestDto dto) {
        ServiceEntity service = ServiceEntity.builder()
                .title(dto.getTitle().trim())
                .category(dto.getCategory().trim())
                .price(dto.getPrice())
                .active(dto.getActive() == null || dto.getActive())
                .build();
        return serviceRepository.save(service);
    }

    @Transactional
    public ServiceEntity update(Long id, ServiceRequestDto dto) {
        ServiceEntity service = getService(id);
        service.setTitle(dto.getTitle().trim());
        service.setCategory(dto.getCategory().trim());
        service.setPrice(dto.getPrice());
        service.setActive(dto.getActive() == null || dto.getActive());
        return serviceRepository.save(service);
    }

    @Transactional
    public void delete(Long id) {
        serviceRepository.delete(getService(id));
    }

    private ServiceEntity getService(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Услуга с id " + id + " не найдена"));
    }
}
