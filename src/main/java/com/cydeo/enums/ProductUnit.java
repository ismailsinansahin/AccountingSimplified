package com.cydeo.enums;

import lombok.Getter;

@Getter
public enum ProductUnit {

    LBS("Libre"),
    GALLON("Gallon"),
    PCS("Pieces"),
    KG("Kilogram"),
    METER("Meter"),
    INCH("Inch"),
    FEET("Feet");

    private final String value;

    ProductUnit(String value) {
        this.value = value;
    }

}
