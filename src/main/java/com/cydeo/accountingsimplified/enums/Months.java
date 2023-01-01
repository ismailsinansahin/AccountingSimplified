package com.cydeo.accountingsimplified.enums;

public enum Months {
    JAN("January"), FEB("February"), MAR("March"), APR("April"), MAY("May"), JUN("June"), JLY("July"), AUG("August"), SEP("September"), OCT("October"), NOV("November"), DEC("December");

    private final String value;

    Months(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}