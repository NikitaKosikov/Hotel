package com.kosnik.domain;

import com.kosnik.service.exception.CompetitorRiskIsFatalException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Date;

@Document("competitor-risks")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompetitorRisk {
    private static final Double criticalRiskFactor = 0.35;
    @Id
    private String id;
    private Double riskFactor;
    private Date date;

    public CompetitorRisk(Double riskFactor, Date date) {
        this.riskFactor = riskFactor;
        this.date = date;
    }

    public void verification() {
        if (this.riskFactor>=criticalRiskFactor){
            throw new CompetitorRiskIsFatalException();
        }
    }
}
