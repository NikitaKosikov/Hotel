package com.kosnik.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosnik.domain.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal price;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatus status;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date orderDate;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Date arrivalDate;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private Date departureDate;
}
