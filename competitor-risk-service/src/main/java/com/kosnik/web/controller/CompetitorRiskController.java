package com.kosnik.web.controller;

import com.kosnik.domain.CompetitorRisk;
import com.kosnik.service.CompetitorRiskService;
import com.kosnik.service.dto.DatePeriod;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/competitor-risk")
@PreAuthorize("permitAll()")
public class CompetitorRiskController {
    private final CompetitorRiskService competitorRiskService;

    @GetMapping
    public ResponseEntity<List<CompetitorRisk>> findRiskByPeriod(DatePeriod datePeriod){
        return ResponseEntity.ok(competitorRiskService.findByPeriod(datePeriod));
    }

    @GetMapping("/actual")
    @CircuitBreaker(name = "search-competitors", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "search-competitors")
    @Retry(name = "search-competitors")
    public ResponseEntity<CompetitorRisk> findActualRisk(){
        return ResponseEntity.ok(competitorRiskService.findActualRisk());
    }
}