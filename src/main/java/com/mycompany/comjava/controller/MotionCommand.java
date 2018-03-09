package com.mycompany.comjava.controller;

public enum MotionCommand {

    STRAIGHT("W"),
    BACK("S"),
    LEFT("A"),
    RIGHT("D"),
    STOP("");

    private String value;

    MotionCommand(String value) {
        this.value = value;
    }

}
