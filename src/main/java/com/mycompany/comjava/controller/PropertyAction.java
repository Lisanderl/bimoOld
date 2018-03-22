package com.mycompany.comjava.controller;

import lombok.Getter;

public enum PropertyAction {

    LIGHT("L"),
    SPEED("S"),
    ECHO("E"),
    VOLTAGE("V"),
    CONNECT("C");

    @Getter
    private String shortName;

    PropertyAction(String shortName) {
        this.shortName = shortName;
    }

    public static String jsonValue(PropertyAction action, int val){

        return "{\"" + action.getShortName() + "\":" + val + "}";

    }
}
