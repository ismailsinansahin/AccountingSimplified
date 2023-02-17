package com.cydeo.accountingsimplified.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
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
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceProductDto {

    private Long id;

    private Integer quantity;

    private BigDecimal price;

    private Integer tax;

    private BigDecimal total;

    @JsonIgnore
    private BigDecimal profitLoss;

    @JsonIgnore
    private int remainingQuantity;

    private InvoiceDto invoice;

    private ProductDto product;

}
