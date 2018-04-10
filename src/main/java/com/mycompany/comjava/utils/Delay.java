package com.mycompany.comjava.utils;

public class Delay {
    public static void ms(long mls){
        try {
            Thread.sleep(mls);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
