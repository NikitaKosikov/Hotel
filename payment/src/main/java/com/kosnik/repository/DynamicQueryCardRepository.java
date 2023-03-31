package com.kosnik.repository;

import com.kosnik.domain.Card;
import com.kosnik.domain.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.NoRepositoryBean;

public interface DynamicQueryCardRepository {
    Page<Card> findAll(SearchQuery searchQuery, Pageable pageable);
}
