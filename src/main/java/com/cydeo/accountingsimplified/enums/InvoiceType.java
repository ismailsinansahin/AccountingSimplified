package com.cydeo.accountingsimplified.enums;

public enum InvoiceType {

    PURCHASE("Purchase Invoice"), SALES("Sales Invoice");

    private String value;

    InvoiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
