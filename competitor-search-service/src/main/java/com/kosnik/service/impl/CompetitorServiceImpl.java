package com.kosnik.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kosnik.domain.Competitor;
import com.kosnik.parser.CompetitorParser;
import com.kosnik.repository.CompetitorRepository;
import com.kosnik.service.CompetitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CompetitorServiceImpl implements CompetitorService {
    private final CompetitorRepository competitorRepository;
    private final CompetitorParser competitorParser;
    @Override
    public List<Competitor> findAllCompetitors() {
        return competitorRepository.findAll();
    }

    @Override
    public void loadCompetitor(String body){
        List<Competitor> competitors = competitorParser.parse(body);
        competitorRepository.saveAll(competitors);
    }
}
