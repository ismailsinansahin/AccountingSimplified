package com.cydeo.accountingsimplified.enums;

import lombok.Getter;

@Getter
public enum ClientVendorType {

    VENDOR("Vendor"), CLIENT("Client");

    private String value;

    ClientVendorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
