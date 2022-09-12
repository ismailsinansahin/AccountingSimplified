package com.cydeo.accountingsimplified.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDto {

    private Long id;
    private Integer quantity;
    private Integer price;
    private Integer tax;
    private Integer amount;
    private Integer profitLoss;
    private InvoiceDto invoice;
    private ProductDto product;

}
