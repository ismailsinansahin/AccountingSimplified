package com.cydeo.accountingsimplified.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDto {

    private Long id;

    @NotNull(message = "Maximum quantity is set to 10 pieces")
    @Max(value = 10)
    private Integer quantity;

    @NotNull(message = "Please enter product price")
    @Min(value = 10)
    private Integer price;

    @NotNull(message = "Maximum tax should be %20 ")
    @Min(value = 10)
    private Integer tax;

    private Integer total;
    private Integer profitLoss;
    private Integer remainingQuantity;
    private InvoiceDto invoice;
    private ProductDto product;

}
