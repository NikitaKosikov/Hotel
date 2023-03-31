package com.kosnik.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("rooms")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Room {

    @Id
    private String id;
    private String numberOfBeds;
    private BigDecimal cost;
    private String photo;

    private ApartmentType apartmentType;

}