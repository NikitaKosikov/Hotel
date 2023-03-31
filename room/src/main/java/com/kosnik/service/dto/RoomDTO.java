package com.kosnik.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosnik.domain.ApartmentType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private String numberOfBeds;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private BigDecimal cost;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String photo;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private ApartmentType apartmentType;

}
