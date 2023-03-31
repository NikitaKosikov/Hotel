package com.kosnik.service.scheduler;

import com.kosnik.service.CompetitorRiskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiskVerification {
    private final CompetitorRiskService competitorRiskService;

    //@Scheduled(cron = "${here.geocoder.retry}")
    public void calculateCompetitorRisk(){
        competitorRiskService.loadRisk();
    }
}
