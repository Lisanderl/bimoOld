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
        mainFrame.getLed().addItemListener(new LedBoxListener());
        mainFrame.getStraight().addActionListener(new ControlButtonListener(mainFrame.getStraight()));
        mainFrame.getBack().addActionListener(new ControlButtonListener(mainFrame.getBack()));
        mainFrame.getLeft().addActionListener(new ControlButtonListener(mainFrame.getLeft()));
        mainFrame.getRight().addActionListener(new ControlButtonListener(mainFrame.getRight()));
        mainFrame.getStop().addActionListener(new ControlButtonListener(mainFrame.getStop()));
        mainFrame.getClearLog().addActionListener(new ControlButtonListener(mainFrame.getClearLog()));
        mainFrame.getLedPlus().addActionListener(new ControlButtonListener(mainFrame.getLedPlus()));
        mainFrame.getTest().addKeyListener(new ActionKeysListener());
    }

// Key interpretator

    private void buttonHunter(String button) {

        switch (button) {
            case "W":
                mainFrame.getStraight().doClick(time);
                if (getCommandOFF().equals("WW")) {
                    setCommandOFF("");
                }
                break;
            case "D":
                mainFrame.getRight().doClick(time);

                if (getCommandOFF().equals("DD")) {
                    setCommandOFF("");
                }
                break;
            case "A":
                mainFrame.getLeft().doClick(time);
                if (getCommandOFF().equals("AA")) {
                    setCommandOFF("");
                }
                break;
            case "S":
                mainFrame.getBack().doClick(time);
                if (getCommandOFF().equals("SS")) {
                    setCommandOFF("");
                }
                break;
            case "C":
                mainFrame.getStop().doClick(time);

                break;
            case "WW":
                mainFrame.getStop().doClick(time);
                if (getCommandON().equals("W")) {
                    setCommandON("");
                }
                break;
            case "AA":
                mainFrame.getStop().doClick(time);
                if (getCommandON().equals("A")) {
                    setCommandON("");
                }
                break;
            case "DD":
                mainFrame.getStop().doClick(time);
                if (getCommandON().equals("D")) {
                    setCommandON("");
                }
                break;
            case "SS":
                mainFrame.getStop().doClick(time);
                if (getCommandON().equals("S")) {
                    setCommandON("");
                }
                break;

        }

    }

    ////Keyboard
    private class ActionKeysListener extends KeyAdapter {

        String commandON;
        String commandOFF;

        @Override
        public void keyPressed(KeyEvent e) {
            commandON = e.getKeyText(e.getKeyCode());

            if (!commandON.equals(getCommandON())) {
                if ((getCommandON().equals("W"))
                        && (commandON.equals("A")
                        || commandON.equals("D"))) {
                    switchCommand=true;
                }else switchCommand=false;
                setCommandON(commandON);
                buttonHunter(commandON);
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
            commandOFF = e.getKeyText(e.getKeyCode()) + e.getKeyText(e.getKeyCode());
            if (!commandOFF.equals(getCommandOFF())) {
                if(switchCommand&&(commandOFF.equals("AA")||commandOFF.equals("DD"))){
                    setCommandON("W");
                buttonHunter("W");
                }else{
                setCommandOFF(commandOFF);
                buttonHunter(commandOFF);
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
                case "Led+":
                    serialPortController.write("L");
                    break;
                //case "Ctrl":
                // serialPortController.write("C");
                // break;
                default:
                    serialPortController.write(button.getText());
                    break;
            }

        }

    }

    //Check Box - Led, Mode, 2x - LIstners
    private class LedBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() == ItemEvent.SELECTED) | (e.getStateChange() == ItemEvent.DESELECTED)) {

                serialPortController.write("L");
            }

        }

    }

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
