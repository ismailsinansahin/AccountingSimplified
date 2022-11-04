package com.cydeo.accountingsimplified.dto;

import com.cydeo.accountingsimplified.enums.ProductUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private Integer quantityInStock;
    private Integer lowLimitAlert;
    private ProductUnit productUnit;
    private CategoryDto category;

}
