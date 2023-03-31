package com.kosnik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kosnik.domain.Competitor;

import java.util.List;

public interface CompetitorService {
    List<Competitor> findAllCompetitors();
    void loadCompetitor(String body) throws JsonProcessingException;
}
