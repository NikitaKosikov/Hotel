package com.kosnik.domain;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompetitorRiskFactory {

    @Value("${hotel.location.latitude}")
    private static Double latitude;
    @Value("${hotel.location.longitude}")
    private static Double longitude;
    private static final Map<List<Integer>, Float> distanceFactor = new HashMap<>(){
        {
            put(List.of(0, 500), 0.5F);
            put(List.of(500, 1000), 0.425F);
            put(List.of(1000, 2000), 0.375F);
            put(List.of(2000, 5000), 0.315F);
        }
    };

    public static CompetitorRisk createCompetitorRisk(List<Competitor> competitors){
        Double riskFactor = calculateRiskFactor(competitors);
        Date date = new Date(new java.util.Date().getTime());
        return new CompetitorRisk(riskFactor, date);
    }

    private static Double calculateRiskFactor(List<Competitor> competitors) {
        double riskFactor = 0.0;
        for (Competitor competitor : competitors){
            int distance = calculateDistanceToCompetitor(competitor);
            riskFactor = calculateRiskBasedOnDistance(riskFactor, distance);
            riskFactor = calculateRiskBasedBuildingType(riskFactor, competitor.getBuildingType());
        }
        return riskFactor;
    }

    private static Integer calculateDistanceToCompetitor(Competitor competitor) {
        double theta = longitude - competitor.getLongitude();
        double distance = 60 * 1.1515 * (180/Math.PI) * Math.acos(
                Math.sin(latitude * (Math.PI/180)) * Math.sin(competitor.getLatitude() * (Math.PI/180)) +
                        Math.cos(latitude * (Math.PI/180)) * Math.cos(competitor.getLatitude() * (Math.PI/180)) * Math.cos(theta * (Math.PI/180))
        );
        return Math.toIntExact(Math.round(distance * 1.609344 * 1000));
    }

    private static Double calculateRiskBasedOnDistance(double riskFactor, int distance) {
        if (distance<=500){
            riskFactor+=0.5;
        } else if (distance<=1000) {
            riskFactor+=0.425;
        } else if (distance<=2000) {
            riskFactor+=0.375;
        }else if (distance<=5000){
            riskFactor+=0.315;
        }
        return riskFactor;
    }

    private static Double calculateRiskBasedBuildingType(double riskFactor, BuildingType buildingType) {
        switch (buildingType){
            case HOTEL -> riskFactor+=0.3;
            case HOSTEL -> riskFactor+=0.1;
        }
        return riskFactor;
    }



}
