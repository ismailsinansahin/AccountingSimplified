package com.cydeo.accountingsimplified.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDto {

    private Long id;

    @NotNull(message = "Quantity is required.")
    @Range(min = 1, max = 10, message = "Maximum order count is 10")
    private int quantity;

    @NotNull(message = "Price is required.")
    @Range(min = 1, message = "Price should be at least $1")
    private BigDecimal price;

    @NotNull(message = "Tax is required.")
    @Range(min = 5, max = 20, message = "Tax should be between %5 and %20")
    private int tax;

    private BigDecimal total;
    private BigDecimal profitLoss;
    private int remainingQuantity;

    private InvoiceDto invoice;

    @NotNull
    @Valid
    private ProductDto product;

}
