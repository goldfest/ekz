package com.exam.servicecenter.service;

import com.exam.servicecenter.dto.ClientRequestDto;
import com.exam.servicecenter.dto.ClientResponseDto;
import com.exam.servicecenter.entity.ClientEntity;
import com.exam.servicecenter.exception.NotFoundException;
import com.exam.servicecenter.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public Page<ClientResponseDto> findAll(String search, Boolean active, Pageable pageable) {
        return clientRepository.findFiltered(search, active, pageable)
                .map(this::toDto);
    }

    @Transactional(readOnly = true)
    public ClientResponseDto findById(Long id) {
        return toDto(getClient(id));
    }

    @Transactional
    public ClientResponseDto create(ClientRequestDto dto) {
        ClientEntity client = ClientEntity.builder()
                .fullName(dto.getFullName().trim())
                .phone(dto.getPhone().trim())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .active(dto.getActive() == null || dto.getActive())
                .registeredAt(LocalDate.now())
                .build();

        return toDto(clientRepository.save(client));
    }

    @Transactional
    public ClientResponseDto update(Long id, ClientRequestDto dto) {
        ClientEntity client = getClient(id);

        client.setFullName(dto.getFullName().trim());
        client.setPhone(dto.getPhone().trim());
        client.setEmail(dto.getEmail());
        client.setAddress(dto.getAddress());

        if (dto.getActive() != null) {
            client.setActive(dto.getActive());
        }

        return toDto(clientRepository.save(client));
    }

    @Transactional
    public void delete(Long id) {
        ClientEntity client = getClient(id);
        client.setActive(false);
        clientRepository.save(client);
    }

    private ClientEntity getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Клиент с id " + id + " не найден"));
    }

    private ClientResponseDto toDto(ClientEntity client) {
        return ClientResponseDto.builder()
                .id(client.getId())
                .fullName(client.getFullName())
                .phone(client.getPhone())
                .email(client.getEmail())
                .address(client.getAddress())
                .active(client.isActive())
                .registeredAt(client.getRegisteredAt())
                .build();
    }
}