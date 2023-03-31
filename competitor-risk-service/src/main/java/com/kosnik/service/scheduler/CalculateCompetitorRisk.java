package com.kosnik.service.scheduler;

import com.kosnik.service.CompetitorRiskService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CalculateCompetitorRisk {
    private final CompetitorRiskService competitorRiskService;

    //@Scheduled(cron = "${here.geocoder.retry}")
    public void calculateCompetitorRisk(){
        competitorRiskService.loadRisk();
    }
}
