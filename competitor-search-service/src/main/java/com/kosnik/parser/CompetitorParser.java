package com.kosnik.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosnik.domain.BuildingType;
import com.kosnik.domain.Competitor;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.format.Parser;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CompetitorParser {
    public List<Competitor> parse(String jsonCompetitors) {
        List<Competitor> competitors = new ArrayList<>();
        JSONObject competitorsJson = new JSONObject(jsonCompetitors);
        JSONArray hotels = competitorsJson.getJSONArray("features");

        for (int i = 0; i < hotels.length(); i++) {
            JSONObject hotel = hotels.getJSONObject(i);
            JSONObject properties = hotel.getJSONObject("properties");

            String name;
            String city;
            String street;
            String county;
            double latitude;
            double longitude;
            try {
                name = properties.getString("name");
                county = properties.getString("country");
                city = properties.getString("city");
                street = properties.getString("street");
                latitude = properties.getDouble("lat");
                longitude = properties.getDouble("lon");

            }catch (JSONException ignore){
                continue;
            }


            JSONArray buildingTypeNode = properties.getJSONArray("categories");
            BuildingType buildingType = extractBuildingType(buildingTypeNode);

            competitors.add(Competitor.builder()
                    .name(name)
                    .country(county)
                    .city(city)
                    .street(street)
                    .buildingType(buildingType)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build());
        }
        return competitors;
    }

    private BuildingType extractBuildingType(JSONArray categories){
        for (int i = 0; i < categories.length(); i++) {
            if (categories.get(i).toString().equalsIgnoreCase("accommodation." + BuildingType.HOTEL)){
                return BuildingType.HOTEL;
            } else if (categories.get(i).toString().equalsIgnoreCase("accommodation." + BuildingType.HOSTEL)) {
                return BuildingType.HOSTEL;
            }
        }
        return null;
    }
}
