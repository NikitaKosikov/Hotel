package com.kosnik.repository;

import com.kosnik.domain.Competitor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitorRepository extends ListCrudRepository<Competitor, String> {
}
