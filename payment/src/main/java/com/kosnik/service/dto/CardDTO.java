package com.kosnik.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosnik.domain.PaymentSystem;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CardDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    private PaymentSystem paymentSystem;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$")
    @Pattern(regexp = "^(62[0-9]{14,17})$")
    private Integer number;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "(0[1-9]|1[0-2])\\/[0-9]{2}")
    private Date expire;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String owner;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^[0-9]{3,4}$")
    private Integer CVV;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal balance;

}
