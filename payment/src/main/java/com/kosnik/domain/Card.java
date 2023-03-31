package com.kosnik.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document("cards")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Card {
    @Id
    private String id;
    private PaymentSystem paymentSystem;
    private Integer number;
    private Date expire;
    private String owner;
    private Integer CVV;
    private BigDecimal balance;
}
