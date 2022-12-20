package com.cydeo.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyApiResponse {

    private LocalDate date;
    private Usd usd;


   @Data
    public static class Usd {
        private BigDecimal eur;
        private BigDecimal gbp;
        private BigDecimal inr;
        private BigDecimal jpy;
        private BigDecimal cad;

    }
}
