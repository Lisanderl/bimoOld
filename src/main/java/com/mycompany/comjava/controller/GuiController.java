/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comjava.controller;

import com.mycompany.comjava.gui.MainFrame;
import com.mycompany.comjava.logger.TextComponentLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;

@Component
public class GuiController {

    private MainFrame mainFrame;
    private SerialPortController serialPortController;
    private TextComponentLogger logger;
    private boolean switchCommand = false; // special field for correct control
    private String commandON = " ";
    private String commandOFF = " ";
    private final int time = 1; // click time
    private Timer t1;

    @Autowired
    public GuiController(MainFrame mainFrame,
                         SerialPortController serialPortController) {

        this.mainFrame=mainFrame;
        this.serialPortController=serialPortController;
        this.logger = TextComponentLogger.getInstance(mainFrame.getLog());
        addSerialPortAdjListeners();
        addControlListeners();

    }

    private void addSerialPortAdjListeners() {

        mainFrame.getRefresh().addActionListener(new ControlButtonListener(mainFrame.getRefresh()));
//Combobox listners
        mainFrame.getCOMPort().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                serialPortController.setPortName((String) e.getItem());
            }
        });

        mainFrame.getPortSpeed().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                serialPortController.setPortSpeed((int) e.getItem());
            }
        });

        mainFrame.getBits().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                serialPortController.setBits((int) e.getItem());
            }
        });

        mainFrame.getStopBits().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                serialPortController.setStopBits((int) e.getItem());
            }
        });

        mainFrame.getParity().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                serialPortController.setParity((int) e.getItem());
            }
        });

    }

    private void addControlListeners(){
        mainFrame.getAdc().addItemListener(new AdcBoxListener());
        mainFrame.getLed().addItemListener((ItemEvent e) -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                mainFrame.getLightSlider().setEnabled(true);
            }
            if(e.getStateChange() == ItemEvent.DESELECTED){
                mainFrame.getLightSlider().setEnabled(false);
            }
        });
        mainFrame.getPwm().addItemListener((ItemEvent e) -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                mainFrame.getSpeedSlider().setEnabled(true);
            }
            if(e.getStateChange() == ItemEvent.DESELECTED){
                mainFrame.getSpeedSlider().setEnabled(false);
            }
        });
        mainFrame.getClearLog().addActionListener(new ControlButtonListener(mainFrame.getClearLog()));
        mainFrame.getTest().addKeyListener(new ActionKeysListener());
    }

// Key interpretator


    ////Keyboard
    private class ActionKeysListener extends KeyAdapter {
       Optional<KeyBoardAction> optionalAction;
        String activeReleasedKey = null;
        String activePressedKey = null;

        @Override
        public void keyPressed(KeyEvent e) {
            String pressedKey = e.getKeyText(e.getKeyCode());
            if(!pressedKey.equals(activePressedKey)){
                System.out.print(pressedKey);
                activePressedKey = pressedKey;
                optionalAction = KeyBoardAction.findActionByButton(activePressedKey);
                if(optionalAction.isPresent()){
                    optionalAction.get().setActive(true);
                    serialPortController.write(String.valueOf(KeyBoardAction.getSumOfActiveActions()));
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            String releasedKey = e.getKeyText(e.getKeyCode());
            if(!releasedKey.equals(activeReleasedKey)){
                activeReleasedKey = releasedKey;
                optionalAction = KeyBoardAction.findActionByButton(activeReleasedKey);
                if(optionalAction.isPresent()){
                    optionalAction.get().setActive(false);
                 serialPortController.write(String.valueOf(KeyBoardAction.getSumOfActiveActions()));
                }
            }
        }

    }

//Buttons
    private class ControlButtonListener implements ActionListener {

        JButton button;

        private ControlButtonListener(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            switch (button.getText()) {
                case "Ref":
                    serialPortController.putPortToBox();
                    break;
                case "Clear":
                    logger.clearLog();
                    break;
            }
        }
    }

    //Check Box - Led, Mode, 2x - LIstners

    private class AdcBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() == ItemEvent.SELECTED)) {
                t1 = new Timer();
                t1.schedule(adcTask(), 3000, 3000);

            }

            if ((e.getStateChange() == ItemEvent.DESELECTED)) {
                t1.cancel();
                t1 = null;

            }

        }

    }

    private TimerTask adcTask() {

        TimerTask tt1;
        tt1 = new TimerTask() {

            @Override
            public void run() {
                //  read(); command

                serialPortController.writeRequest("G");
            }
        };
        return tt1;

    }

    public String getCommandON() {
        return commandON;
    }

    public void setCommandON(String commandON) {
        this.commandON = commandON;
    }

    public String getCommandOFF() {
        return commandOFF;
    }

    public void setCommandOFF(String commandOFF) {
        this.commandOFF = commandOFF;
    }

}
