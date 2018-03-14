package com.mycompany.comjava.controller;

import java.util.function.Supplier;

public final class Command {

    public enum Motion implements Supplier<String>{
        STRAIGHT("W"),
        BACK("S"),
        LEFT("A"),
        RIGHT("D"),
        STOP("");

        private String value;

        Motion(String value) {
            this.value = value;
        }

        @Override
        public String get() {
            return value;
        }

    }

    public enum Light implements Supplier<String>{
        LIGHT_ON("+"),
        LIGHT_OF("-");

        private String value;

        Light(String value) {
            this.value = value;
        }

        @Override
        public String get() {
            return value;
        }
    }


}
