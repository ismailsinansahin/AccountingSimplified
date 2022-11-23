package com.cydeo.accountingsimplified.dto;

import com.cydeo.accountingsimplified.enums.CompanyStatus;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;

    @NotBlank(message = "Title is a required field.")
    @Size(max = 100, min = 2, message = "Title should be 2-100 characters long.")
    private String title;

    @Pattern(regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$" // +111 (202) 555-0125  +1 (202) 555-0125
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"                                  // +111 123 456 789
            + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$", message = "Phone number is required field and may be in any valid phone number format.")
    // +111 123 45 67 89
    private String phone;

    @NotBlank(message = "Website is a required field.")
   // @Pattern(regexp = "^http(s{0,1})://[a-zA-Z0-9/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9/\\&\\?\\=\\-\\.\\~\\%]*", message = "Website should have a valid format.")
    @URL(message = "url validation")
    private String website;

    @NotNull
    @Valid
    private AddressDto address;
    private CompanyStatus companyStatus;


//    String urlPattern = new RegExp('^(https?:\/\/)?'+ // validate protocol
//            '((([a-z\d]([a-z\d-][a-z\d]))\.)+[a-z]{2,}|'+ // validate domain name
//            '((\d{1,3}\.){3}\d{1,3}))'+ // validate OR ip (v4) address
//            '(\:\d+)?(\/[-a-z\d%.~+])'+ // validate port and path
//            '(\?[;&a-z\d%.~+=-])?'+ // validate query string
//            '(\#[-a-z\d_])?$','i'); // validate fragment locator


}
