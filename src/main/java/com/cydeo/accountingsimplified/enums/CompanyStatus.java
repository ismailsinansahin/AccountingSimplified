package com.cydeo.accountingsimplified.enums;

import lombok.Getter;

@Getter
public enum CompanyStatus {

    ACTIVE("Active"),
    PASSIVE("Passive");

    private final String value;

    CompanyStatus(String value) {
        this.value = value;
    }

}
