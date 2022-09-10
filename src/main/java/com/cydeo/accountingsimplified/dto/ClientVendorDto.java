package com.cydeo.accountingsimplified.dto;

import com.cydeo.accountingsimplified.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientVendorDto {

    private Long id;
    private String companyName;
    private String phone;
    private String website;
    private ClientVendorType clientVendorType;
    private AddressDto address;
    private CompanyDto company;

}
