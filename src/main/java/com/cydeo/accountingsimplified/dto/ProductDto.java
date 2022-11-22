package com.cydeo.accountingsimplified.dto;

import com.cydeo.accountingsimplified.enums.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = "Product Name is a required field.")
    @Size(max = 100, min = 2, message = "Product Name should be 2-100 characters long.")
    private String name;

    private Integer quantityInStock;

    @NotNull(message = "Low Limit Alert is a required field.")
    @Range(min = 1, message = "Low Limit Alert should be at least 1.")
    private Integer lowLimitAlert;

    @NotNull(message = "Product Unit is a required field.")
    private ProductUnit productUnit;

    @NotNull(message = "Category is a required field.")
    private CategoryDto category;

}
