package com.mycompany.comjava.logger;


import org.springframework.stereotype.Component;

import javax.swing.text.JTextComponent;
import java.awt.*;

public class TextComponentLogger {

    private static TextComponentLogger singleton;

    private JTextComponent textComponent;

    private TextComponentLogger(JTextComponent textComponent) {
        this.textComponent = textComponent;
    }

    public static TextComponentLogger getInstance(JTextComponent textComponent){
        if (singleton==null){
            singleton = new TextComponentLogger(textComponent);
        }
        return singleton;
    }

    public void INFO(String data){
        writeToLog("[INFO]" + data);
    }

    public void ERROR(String data){
        writeToLog("[ERROR]" + data);
    }

    private void writeToLog(String data) {
        textComponent.setText( textComponent.getText() + "\n" + data);
    }

    public void clearLog() {
        textComponent.setText("");
    }

}
