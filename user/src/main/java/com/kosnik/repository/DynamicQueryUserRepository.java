package com.kosnik.repository;

import com.kosnik.domain.User;
import com.kosnik.domain.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DynamicQueryUserRepository {
    Page<User> findAll(SearchQuery searchQuery, Pageable pageable);

}
