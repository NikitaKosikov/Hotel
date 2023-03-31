package com.kosnik.repository;

import com.kosnik.domain.Room;
import com.kosnik.domain.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DynamicQueryRoomRepository {
    Page<Room> findAll(SearchQuery searchQuery, Pageable pageable);

}
