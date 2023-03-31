package com.kosnik.repository;

import com.kosnik.domain.CompetitorRisk;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitorRiskRepository extends CrudRepository<CompetitorRisk, String> {
    @Query("{$orderby: {id:-1}, $limit:1 }")
    Optional<CompetitorRisk> findFirstOrderByIdDesc();

    List<CompetitorRisk> findByDateBetween(Date from, Date to);
}
