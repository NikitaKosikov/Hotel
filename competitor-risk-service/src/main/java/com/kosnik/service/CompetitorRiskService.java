package com.kosnik.service;

import com.kosnik.domain.CompetitorRisk;
import com.kosnik.service.dto.DatePeriod;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CompetitorRiskService {
    CompetitorRisk loadRisk();
    CompetitorRisk findActualRisk();
    void competitorRiskVerification();
    List<CompetitorRisk> findByPeriod(DatePeriod datePeriod);
}
