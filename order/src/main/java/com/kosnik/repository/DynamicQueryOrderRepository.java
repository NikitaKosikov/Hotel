package com.kosnik.repository;

import com.kosnik.domain.Order;
import com.kosnik.domain.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.NoRepositoryBean;

public interface DynamicQueryOrderRepository {
    Page<Order> findAll(SearchQuery searchQuery, Pageable pageable);
}
