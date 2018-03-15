package com.mycompany.comjava.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Optional;

public enum KeyBoardAction {

    GO_STRAIGHT("W", 1),
    GO_BACK("S", 2),
    TURN_LEFT("A", 4),
    TURN_RIGHT("D", 7),
    STOP("Space", 10),
    ALARM("Ctrl", 20);

    @Getter
    private String button;
    @Getter
    private int actionCode;
    @Getter
    @Setter
    private boolean active;

    KeyBoardAction(String buttom, int actionCode) {
        this.button = buttom;
        this.actionCode = actionCode;
        this.active = false;
    }

    public static int getSumOfActiveActions(){
        return  Arrays.stream(values())
                .filter(KeyBoardAction::isActive)
                .mapToInt(KeyBoardAction::getActionCode)
                .sum();
    }

    public static Optional<KeyBoardAction> findActionByButton(String buttonName){
        Optional<KeyBoardAction> result = Arrays.stream(values()).filter(val -> val.getButton().equals(buttonName)).findAny();
        return result.isPresent() ? result : Optional.empty();
    }

    public static String jsonValue( int val){

        return "{\"A\":" + val + "}";

    }

}
