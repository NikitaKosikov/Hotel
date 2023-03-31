package com.kosnik.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("competitors")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Competitor {
    @Id
    private String id;
    private String name;
    private String city;
    private String country;
    private String street;
    private BuildingType buildingType;
    private Double longitude;
    private Double latitude;

}
