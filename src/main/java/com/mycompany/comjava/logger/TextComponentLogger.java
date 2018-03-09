package com.mycompany.comjava.logger;


import org.springframework.stereotype.Component;

import javax.swing.text.JTextComponent;
import java.awt.*;

@Component
public class TextComponentLogger {

    private JTextComponent textComponent;

    public TextComponentLogger(JTextComponent textComponent) {
        this.textComponent = textComponent;
    }

    public void INFO(String data){
        textComponent.setSelectedTextColor(Color.BLUE);
        writeToLog(data);
    }

    public void ERROR(String data){
        textComponent.setSelectedTextColor(Color.RED);
        writeToLog(data);
    }

    private void writeToLog(String data) {
        textComponent.setText( textComponent.getText() + "\n" + data);
    }

    public void clearLog() {
        textComponent.setText("");
    }

}
