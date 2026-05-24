package com.exam.servicecenter.controller;

import com.exam.servicecenter.dto.ClientRequestDto;
import com.exam.servicecenter.dto.ClientResponseDto;
import com.exam.servicecenter.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public Page<ClientResponseDto> findAll(@RequestParam(required = false) String search,
                                           @RequestParam(required = false) Boolean active,
                                           @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return clientService.findAll(search, active, pageable);
    }

    @GetMapping("/{id}")
    public ClientResponseDto findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PostMapping
    public ClientResponseDto create(@Valid @RequestBody ClientRequestDto dto) {
        return clientService.create(dto);
    }

    @PutMapping("/{id}")
    public ClientResponseDto update(@PathVariable Long id, @Valid @RequestBody ClientRequestDto dto) {
        return clientService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientService.delete(id);
    }
}