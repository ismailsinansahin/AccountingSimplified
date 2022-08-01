package com.cydeo.accountingsimplified.enums;

import lombok.Getter;

@Getter
public enum Country {

    UNITED_STATES_OF_AMERICA("United States Of America"),
    UNITED_KINGDOM("United Kingdom"),
    GERMANY("Germany"),
    FRANCE("France"),
    TURKEY("Turkey"),
    BRASIL("Brasil"),
    ITALY("Italy"),
    NETHERLAND("Netherland"),
    CANADA("Canada");

    private final String value;

    Country(String value) {
        this.value = value;
    }

}
