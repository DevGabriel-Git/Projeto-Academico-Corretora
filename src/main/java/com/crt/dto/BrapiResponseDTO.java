package com.crt.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class BrapiResponseDTO {
    private List<Result> results;

    @Getter
    @Setter
    public static class Result {
        private String symbol;
        private String longName;
        private String currency;
        private BigDecimal regularMarketPrice;
    }
}