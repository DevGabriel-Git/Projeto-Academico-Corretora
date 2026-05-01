package com.crt.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class TwelveDataResponseDTO {

    private String symbol;
    private String name;
    private String currency;
    private BigDecimal close;
}