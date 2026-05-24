package com.exam.servicecenter.service;

import com.exam.servicecenter.dto.ClientRequestDto;
import com.exam.servicecenter.entity.ClientEntity;
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
    public Page<ClientEntity> findAll(String search, Boolean active, Pageable pageable) {
        return clientRepository.findFiltered(search, active, pageable);
    }

    @Transactional(readOnly = true)
    public ClientEntity findById(Long id) {
        return getClient(id);
    }

    @Transactional
    public ClientEntity create(ClientRequestDto dto) {
        ClientEntity client = ClientEntity.builder()
                .fullName(dto.getFullName().trim())
                .phone(dto.getPhone().trim())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .active(dto.getActive() == null || dto.getActive())
                .registeredAt(LocalDate.now())
                .build();
        return clientRepository.save(client);
    }

    @Transactional
    public ClientEntity update(Long id, ClientRequestDto dto) {
        ClientEntity client = getClient(id);
        client.setFullName(dto.getFullName().trim());
        client.setPhone(dto.getPhone().trim());
        client.setEmail(dto.getEmail());
        client.setAddress(dto.getAddress());
        client.setActive(dto.getActive() == null || dto.getActive());
        return clientRepository.save(client);
    }

    @Transactional
    public void delete(Long id) {
        clientRepository.delete(getClient(id));
    }

    private ClientEntity getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Клиент с id " + id + " не найден"));
    }
}
