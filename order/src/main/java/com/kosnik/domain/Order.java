package com.kosnik.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document("orders")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Order {
    @Id
    private String id;
    private BigDecimal price;
    private OrderStatus status;
    private Date orderDate;
    private Date arrivalDate;
    private Date departureDate;
}
