package com.exam.servicecenter.controller;

import com.exam.servicecenter.dto.OrderRequestDto;
import com.exam.servicecenter.dto.OrderResponseDto;
import com.exam.servicecenter.enums.OrderStatus;
import com.exam.servicecenter.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public Page<OrderResponseDto> findAll(@RequestParam(required = false) String search,
                                          @RequestParam(required = false) OrderStatus status,
                                          @RequestParam(required = false) Long clientId,
                                          @RequestParam(required = false) Long employeeId,
                                          @RequestParam(required = false) Long serviceId,
                                          Pageable pageable) {
        return orderService.findAll(search, status, clientId, employeeId, serviceId, pageable);
    }

    @GetMapping("/{id}")
    public OrderResponseDto findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @PostMapping
    public OrderResponseDto create(@Valid @RequestBody OrderRequestDto dto) {
        return orderService.create(dto);
    }

    @PutMapping("/{id}")
    public OrderResponseDto update(@PathVariable Long id, @Valid @RequestBody OrderRequestDto dto) {
        return orderService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }
}
