/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comjava.controller;

import com.mycompany.comjava.config.KeyBoardAction;
import com.mycompany.comjava.config.PropertyAction;
import com.mycompany.comjava.gui.MainFrame;
import com.mycompany.comjava.utils.TextComponentLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.Timer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;

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

        mainFrame.getLed().addItemListener((ItemEvent e) -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                getValueFromSliderAndWriteItToSerial(PropertyAction.LIGHT, mainFrame.getLightSlider());
                mainFrame.getLightSlider().setEnabled(true);
            }
            if(e.getStateChange() == ItemEvent.DESELECTED){
                mainFrame.getLightSlider().setEnabled(false);
                serialPortController.write(PropertyAction.jsonValue(PropertyAction.LIGHT, 0));
            }
        });

        mainFrame.getPwm().addItemListener((ItemEvent e) -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                getValueFromSliderAndWriteItToSerial(PropertyAction.SPEED, mainFrame.getSpeedSlider());
                mainFrame.getSpeedSlider().setEnabled(true);
            }
            if(e.getStateChange() == ItemEvent.DESELECTED){
                mainFrame.getSpeedSlider().setEnabled(false);
            }
        });

        mainFrame.getEcho().addItemListener((ItemEvent e) -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                serialPortController.write(PropertyAction.jsonValue(PropertyAction.ECHO, (int)mainFrame.getEchoRange().getSelectedItem()));

            }
            if(e.getStateChange() == ItemEvent.DESELECTED){
                serialPortController.write(PropertyAction.jsonValue(PropertyAction.ECHO, 0));
            }
        });

        mainFrame.getAdc().addItemListener((ItemEvent e) -> {
            if(e.getStateChange() == ItemEvent.SELECTED){
                serialPortController.write(PropertyAction.jsonValue(PropertyAction.VOLTAGE, 1));
            }
            if(e.getStateChange() == ItemEvent.DESELECTED){
                serialPortController.write(PropertyAction.jsonValue(PropertyAction.VOLTAGE, 0));
                mainFrame.getBatteryVoltage().setText("");
            }
        });

        mainFrame.getClearLog().addActionListener(new ControlButtonListener(mainFrame.getClearLog()));

        mainFrame.getTest().addKeyListener(new ActionKeysListener());

        mainFrame.getLightSlider().addChangeListener((ChangeEvent e) -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                getValueFromSliderAndWriteItToSerial(PropertyAction.LIGHT, source);
            }
        });

        mainFrame.getSpeedSlider().addChangeListener((ChangeEvent e) -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                getValueFromSliderAndWriteItToSerial(PropertyAction.SPEED, source);
            }
        });

    }

   private void getValueFromSliderAndWriteItToSerial(PropertyAction action, JSlider slider){
       serialPortController.write(PropertyAction.jsonValue(action, slider.getValue()));
   }

    ////Keyboard
    private class ActionKeysListener extends KeyAdapter {
       Optional<KeyBoardAction> optionalAction;
        String activeReleasedKey = "";
        String activePressedKey = "";

        @Override
        public void keyPressed(KeyEvent e) {
            String pressedKey = e.getKeyText(e.getKeyCode());
            if(!pressedKey.equals(activePressedKey)){
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
            keyEraser();
        }

        private void keyEraser() {
            if(activeReleasedKey.equals(activePressedKey)){
                activeReleasedKey = "";
                activePressedKey= "";

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
                    serialPortController.putPortNameToBox();
                    break;
                case "Clear":
                    logger.clearLog();
                    break;
            }
        }
    }

}
