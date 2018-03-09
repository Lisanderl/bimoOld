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

    @Autowired
    private final MainFrame view;
    @Autowired
    private final SerialPortController model;
    @Autowired
    private TextComponentLogger logger;
    private boolean switchCommand = false; // special field for corect control
    private String commandON = " ";
    private String commandOFF = " ";
    private final int time = 1; // click time
    private Timer t1;

    public GuiController(MainFrame view) {

        this.view = view;
        model = this.view.getModel();
        addListners();

    }

    private void addListners() {

        view.getAdc().addItemListener(new AdcBoxListener());

        view.getLed().addItemListener(new LedBoxListener());

        view.getStraight().addActionListener(new ControlButtonListener(view.getStraight()));
        view.getBack().addActionListener(new ControlButtonListener(view.getBack()));
        view.getLeft().addActionListener(new ControlButtonListener(view.getLeft()));
        view.getRight().addActionListener(new ControlButtonListener(view.getRight()));
        view.getStop().addActionListener(new ControlButtonListener(view.getStop()));
        view.getRefresh().addActionListener(new ControlButtonListener(view.getRefresh()));
        view.getClearLog().addActionListener(new ControlButtonListener(view.getClearLog()));
        view.getLedPlus().addActionListener(new ControlButtonListener(view.getLedPlus()));
        view.getTest().addKeyListener(new ActionKeysListener());

//Combobox listners
        view.getCOMPort().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                model.setPortName((String) e.getItem());
            }
        });

        view.getPortSpeed().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                model.setPortSpeed((int) e.getItem());

            }
        });

        view.getBits().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                model.setBits((int) e.getItem());
            }
        });

        view.getStopBits().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                model.setStopBits((int) e.getItem());
            }
        });

        view.getParity().addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                model.setParity((int) e.getItem());
            }
        });

    }
// Key interpretator

    private void buttonHunter(String button) {

        switch (button) {
            case "W":
                view.getStraight().doClick(time);
                if (getCommandOFF().equals("WW")) {
                    setCommandOFF("");
                }
                break;
            case "D":
                view.getRight().doClick(time);

                if (getCommandOFF().equals("DD")) {
                    setCommandOFF("");
                }
                break;
            case "A":
                view.getLeft().doClick(time);
                if (getCommandOFF().equals("AA")) {
                    setCommandOFF("");
                }
                break;
            case "S":
                view.getBack().doClick(time);
                if (getCommandOFF().equals("SS")) {
                    setCommandOFF("");
                }
                break;
            case "C":
                view.getStop().doClick(time);

                break;
            case "WW":
                view.getStop().doClick(time);
                if (getCommandON().equals("W")) {
                    setCommandON("");
                }
                break;
            case "AA":
                view.getStop().doClick(time);
                if (getCommandON().equals("A")) {
                    setCommandON("");
                }
                break;
            case "DD":
                view.getStop().doClick(time);
                if (getCommandON().equals("D")) {
                    setCommandON("");
                }
                break;
            case "SS":
                view.getStop().doClick(time);
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
                      System.out.println("Test 1 OK");
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
                    System.out.println("Test 2 OK");
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
                    model.putPortToBox();
                    break;
                case "Clear":
                    logger.clearLog();
                    break;
                case "Led+":
                    model.write("L");
                    break;
                //case "Ctrl":
                // model.write("C");
                // break;
                default:
                    model.write(button.getText());
                    break;
            }

        }

    }

    //Check Box - Led, Mode, 2x - LIstners
    private class LedBoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() == ItemEvent.SELECTED) | (e.getStateChange() == ItemEvent.DESELECTED)) {

                model.write("L");
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

                model.readRequest("G");
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
