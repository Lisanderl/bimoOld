package com.mycompany.comjava.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public enum Action {

    GO_STRAIGHT("W", 1),
    GO_BACK("S", 2),
    TURN_LEFT("A", 4),
    TURN_RIGHT("D", 7);

    @Getter
    private String button;
    @Getter
    private int actionCode;
    @Getter
    @Setter
    private boolean active;

    Action(String buttom, int actionCode) {
        this.button = buttom;
        this.actionCode = actionCode;
        this.active = false;
    }


    public static int getSumOfActiveActions(){
        return  Arrays.stream(values())
                .filter(Action::isActive)
                .mapToInt(Action::getActionCode)
                .sum();
    }

}
