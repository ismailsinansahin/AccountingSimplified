package com.cydeo.accountingsimplified.dto;

import com.cydeo.accountingsimplified.enums.ProductUnit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDto {

    private Long id;

    private String name;

    private Integer quantityInStock;

    private Integer lowLimitAlert;

    private ProductUnit productUnit;

    private CategoryDto category;

}
