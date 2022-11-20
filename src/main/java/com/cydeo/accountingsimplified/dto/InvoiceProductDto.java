package com.cydeo.accountingsimplified.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDto {

    private Long id;
    private int quantity;
    private BigDecimal price;
    private int tax;
    private BigDecimal total;
    private BigDecimal profitLoss;
    private int remainingQuantity;
    private InvoiceDto invoice;
    private ProductDto product;

}
