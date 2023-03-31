package com.kosnik.repository;

import com.kosnik.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>, DynamicQueryOrderRepository {
}