package com.cydeo.dto.addressApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {
    @JsonProperty("city_name")
    private String cityName;


}
/*
 [
    {
        "city_name": "Alabaster"
    },
    {
        "city_name": "Albertville"
    },
    {
        "city_name": "Alexander City"
    },
    {
        "city_name": "Anniston"
 */