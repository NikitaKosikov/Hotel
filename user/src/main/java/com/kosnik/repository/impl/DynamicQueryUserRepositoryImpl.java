package com.kosnik.repository.impl;

import com.kosnik.domain.User;
import com.kosnik.repository.DynamicQueryUserRepository;
import com.kosnik.domain.Filter;
import com.kosnik.domain.SearchQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DynamicQueryUserRepositoryImpl implements DynamicQueryUserRepository{
    private final MongoTemplate mongoTemplate;
    @Override
    public Page<User> findAll(SearchQuery searchQuery, final Pageable pageable) {
        Query query = build(searchQuery);
        if (pageable!=null){
            query.with(pageable);
        }
        if (searchQuery.getSort()!=null){
            query.with(searchQuery.getSort());
        }
        List<User> users = mongoTemplate.find(query, User.class);
        return new PageImpl<>(users);
    }

    private Query build(SearchQuery searchQuery){
        Query query = new Query();
        List<Criteria> criteria = setUpFilters(searchQuery.getFilters());
        criteria.forEach(query::addCriteria);
        return query;
    }

    private List<Criteria> setUpFilters(List<Filter> filters) {
        List<Criteria> criteria = new ArrayList<>();
        for (Filter filter : filters) {
            String field = filter.getField();
            String operation = filter.getOperation();
            if ("email".equals(field)){
                switch (operation){
                    case "lt" -> criteria.add(Criteria.where(field).lt(filter.getValue()));
                    case "lte" -> {
                        criteria.add(Criteria.where(field).lt(filter.getValue()));
                        criteria.add(Criteria.where(field).is(filter.getValue()));
                    }
                    case "gt" -> criteria.add(Criteria.where(field).gt(filter.getValue()));
                    case "gte" -> {
                        criteria.add(Criteria.where(field).gt(filter.getValue()));
                        criteria.add(Criteria.where(field).is(filter.getValue()));
                    }
                    case "=" -> criteria.add(Criteria.where(field).is(filter.getValue()));
                }
            }
        }
        return criteria;
    }

}
