package com.exam.servicecenter.controller;

import com.exam.servicecenter.dto.ClientRequestDto;
import com.exam.servicecenter.entity.ClientEntity;
import com.exam.servicecenter.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public Page<ClientEntity> findAll(@RequestParam(required = false) String search,
                                      @RequestParam(required = false) Boolean active,
                                      Pageable pageable) {
        return clientService.findAll(search, active, pageable);
    }

    @GetMapping("/{id}")
    public ClientEntity findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PostMapping
    public ClientEntity create(@Valid @RequestBody ClientRequestDto dto) {
        return clientService.create(dto);
    }

    @PutMapping("/{id}")
    public ClientEntity update(@PathVariable Long id, @Valid @RequestBody ClientRequestDto dto) {
        return clientService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }
}
