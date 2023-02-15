package com.cydeo.accountingsimplified.dto;

import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientVendorDto {

    private Long id;
    private String clientVendorName;
    private String phone;
    private String website;
    private ClientVendorType clientVendorType;
    private AddressDto address;
    private CompanyDto company;
}
