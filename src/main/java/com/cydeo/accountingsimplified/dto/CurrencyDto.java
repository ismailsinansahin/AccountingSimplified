package com.cydeo.accountingsimplified.dto;



import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;


@Data
@Builder
public class CurrencyDto {

    private BigDecimal euro;
    private BigDecimal britishPound;
    private BigDecimal indianRupee;
    private BigDecimal japaneseYen;
    private BigDecimal canadianDollar;

}

