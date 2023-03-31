package com.kosnik.service.impl;

import com.kosnik.domain.Order;
import com.kosnik.domain.OrderStatus;
import com.kosnik.domain.SearchQuery;
import com.kosnik.service.OrderService;
import com.kosnik.service.converter.SearchQueryConverter;
import com.kosnik.service.event.OrderPlacedEvent;
import com.kosnik.service.exception.OrderForDateAlreadyTakenException;
import com.kosnik.service.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import com.kosnik.repository.OrderRepository;
import com.kosnik.service.dto.OrderDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    @Override
    public List<OrderDTO> findAll(Map<String, String> params, Pageable pageable) {
        SearchQuery searchQuery = SearchQueryConverter.convert(params);
        Page<Order> orders = orderRepository.findAll(searchQuery, pageable);
        return modelMapper.map(orders.getContent(), new TypeToken<List<OrderDTO>>(){}.getType());
    }

    @Override
    public OrderDTO find(String id) throws OrderNotFoundException {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(o -> modelMapper.map(o, OrderDTO.class))
                .orElseThrow(() -> new OrderNotFoundException("Order with id=" + id + " doesn't exist"));
    }

    @Override
    public OrderDTO save(OrderDTO orderDTO) throws OrderForDateAlreadyTakenException {
        if (orderDTO.getArrivalDate().before(orderDTO.getDepartureDate())){
            throw new OrderForDateAlreadyTakenException("Order date is invalid");
        }
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.WAITING);
        order = orderRepository.save(order);
        kafkaTemplate.send("notification",
                new OrderPlacedEvent(order.getOrderDate(), order.getArrivalDate(), order.getDepartureDate()));
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    @Transactional
    //TODO equals order in db and external
    public OrderDTO update(OrderDTO updateOrderDTO, String id) throws OrderNotFoundException, OrderForDateAlreadyTakenException {
        if (updateOrderDTO.getArrivalDate().before(updateOrderDTO.getDepartureDate())){
            throw new OrderForDateAlreadyTakenException("Order date is invalid");
        }
        OrderDTO orderDTO = find(id);
        Order order = modelMapper.map(orderDTO, Order.class);
        order.setId(id);
        order.setOrderDate(new Date());
        order.setPrice(updateOrderDTO.getPrice());
        order.setArrivalDate(updateOrderDTO.getArrivalDate());
        order.setDepartureDate(updateOrderDTO.getDepartureDate());
        order = orderRepository.save(order);
        return modelMapper.map(order, OrderDTO.class);
    }

}
