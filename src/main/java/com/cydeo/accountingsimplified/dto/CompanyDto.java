package com.cydeo.accountingsimplified.dto;

import com.cydeo.accountingsimplified.enums.CompanyStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;
    private String title;
    private String phone;
    private String website;
    private AddressDto address;
    private CompanyStatus companyStatus;

}
