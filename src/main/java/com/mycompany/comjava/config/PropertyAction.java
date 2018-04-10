package com.mycompany.comjava.config;

import lombok.Getter;

import java.util.Arrays;

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

    public static String isStrContainsShortValue(String data){
        return Arrays.stream(values()).map(val -> val.shortName).filter(shortValue -> data.contains(shortValue)).findAny().orElse("");
    }

    public static String jsonValue(PropertyAction action, int val){

        return "{\"" + action.getShortName() + "\":" + val + "}";
    }

    public static String jsonValue(PropertyAction action, boolean val){

        return "{\"" + action.getShortName() + "\":" + val + "}";
    }
}
