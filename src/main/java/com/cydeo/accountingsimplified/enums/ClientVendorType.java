package com.cydeo.accountingsimplified.enums;

import lombok.Getter;

@Getter
public enum ClientVendorType {

    VENDOR("Vendor"),
    CLIENT("Client");

    private final String value;

    ClientVendorType(String value) {
        this.value = value;
    }

}
