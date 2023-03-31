package com.kosnik.service.impl;

import com.kosnik.domain.Competitor;
import com.kosnik.domain.CompetitorRisk;
import com.kosnik.domain.CompetitorRiskFactory;
import com.kosnik.repository.CompetitorRiskRepository;
import com.kosnik.service.CompetitorRiskService;
import com.kosnik.service.dto.DatePeriod;
import com.kosnik.service.exception.CompetitorRiskNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitorRiskServiceImpl implements CompetitorRiskService {
    private final CompetitorRiskRepository competitorRiskRepository;
    private final RestTemplate restTemplate;

    @Override
    public CompetitorRisk findActualRisk() {
        return competitorRiskRepository.findFirstOrderByIdDesc()
                .orElseThrow(()-> new CompetitorRiskNotFoundException("Competitor risk not found"));
    }

    @Override
    public void competitorRiskVerification() {
        CompetitorRisk competitorRisk = competitorRiskRepository.findFirstOrderByIdDesc()
                .orElseThrow(()-> new CompetitorRiskNotFoundException("Competitor risk not found"));
        competitorRisk.verification();
    }

    @Override
    public List<CompetitorRisk> findByPeriod(DatePeriod datePeriod) {
        return competitorRiskRepository.findByDateBetween(datePeriod.from(), datePeriod.to());
    }

    @Override
    public CompetitorRisk loadRisk() {
        List<Competitor> competitors = restTemplate
                .exchange("http://SEARCH-COMPETITOR-SERVICE/api/v1/competitors", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<Competitor>>(){})
                .getBody();

        CompetitorRisk competitorRisk = CompetitorRiskFactory.createCompetitorRisk(competitors);
        return competitorRiskRepository.save(competitorRisk);
    }
}
