package com.cydeo.accountingsimplified.dto.addressApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Country {


    @JsonProperty("country_name")
    private String countryName;
    @JsonProperty("country_short_name")
    private String countryShortName;
    @JsonProperty("country_phone_code")
    private int countryPhoneCode;


}
/*
 {
        "country_name": "United States",
        "country_short_name": "US",
        "country_phone_code": 1
    },
 */