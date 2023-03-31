package com.kosnik.web.controller;

import com.kosnik.service.OrderService;
import com.kosnik.service.exception.OrderForDateAlreadyTakenException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import com.kosnik.service.dto.OrderDTO;
import com.kosnik.service.exception.OrderNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAll(
            @RequestParam(required = false) Map<String
                    ,String> params, Pageable pageable){
        List<OrderDTO> orders = orderService.findAll(params, pageable);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> find(@PathVariable("id") String id) throws OrderNotFoundException {
        OrderDTO order = orderService.find(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    @CircuitBreaker(name = "mail", fallbackMethod = "fallbackMethodNotification")
    @TimeLimiter(name = "mail")
    @Retry(name = "mail")
    public ResponseEntity<OrderDTO> save(@RequestBody OrderDTO order) throws OrderForDateAlreadyTakenException {
        order = orderService.save(order);
        return ResponseEntity.ok(order);
    }
    @PutMapping("/{id}")
    @CircuitBreaker(name = "email", fallbackMethod = "fallbackMethodNotification")
    @TimeLimiter(name = "email")
    @Retry(name = "email")
    public ResponseEntity<OrderDTO> update(@PathVariable("id") String id, @RequestBody OrderDTO order) throws OrderNotFoundException, OrderForDateAlreadyTakenException {
        order = orderService.update(order, id);
        return ResponseEntity.ok(order);
    }
}
