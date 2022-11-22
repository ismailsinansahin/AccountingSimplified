package com.cydeo.accountingsimplified.dto;

import com.cydeo.accountingsimplified.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientVendorDto {

    private Long id;

    @NotBlank(message = "Company Name is a required field.")
    @Size(min = 2, max = 50, message = "Company Name should have 2-50 characters long.")
    private String companyName;

    //    @NotBlank // @Pattern is enough to check if it is not blank
//    @Pattern(regexp = "^1-[0-9]{3}?-[0-9]{3}?-[0-9]{4}$")                         //  format "1-xxx-xxx-xxxx"
//    imported from https://www.baeldung.com/java-regex-validate-phone-numbers :
    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" // +111 (202) 555-0125  +1 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"                                  // +111 123 456 789
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Phone is required field and may be in any valid phone number format.")
    private String phone;

    @Pattern(regexp = "^http(s{0,1})://[a-zA-Z0-9/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9/\\&\\?\\=\\-\\.\\~\\%]*", message = "Website should have a valid format.")
    private String website;

    @NotNull(message = "Please select type")
    private ClientVendorType clientVendorType;

    @NotNull
    @Valid
    private AddressDto address;

    private CompanyDto company;
}
