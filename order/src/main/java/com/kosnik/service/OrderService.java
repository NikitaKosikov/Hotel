package com.kosnik.service;

import com.kosnik.service.dto.OrderDTO;
import com.kosnik.service.exception.OrderForDateAlreadyTakenException;
import com.kosnik.service.exception.OrderNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface OrderService {
    List<OrderDTO> findAll(Map<String, String> params, Pageable pageable);

    OrderDTO find(String id) throws OrderNotFoundException;
    OrderDTO save(OrderDTO orderDTO) throws OrderForDateAlreadyTakenException;
    OrderDTO update(OrderDTO updateOrderDTO, String id) throws OrderNotFoundException, OrderForDateAlreadyTakenException;


}
