package com.cydeo.accountingsimplified.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto {

    private Long id;
    private String description;

    @JsonIgnore
    private CompanyDto company;

    @JsonIgnore
    private boolean hasProduct;
}
