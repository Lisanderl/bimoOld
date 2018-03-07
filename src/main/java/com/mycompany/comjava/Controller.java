/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comjava;

import com.mycompany.comjava.gui.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;

public class Controller {

    private final MainFrame view;
    private final SerialPortModel model;
    private boolean switchCommand = false; // special field for corect control
    private String commandON = " ";
    private String commandOFF = " ";
    private final int time = 1; // click time
    private Timer t1;

    public Controller(MainFrame view) {

        this.view = view;
        model = this.view.getModel();
        addListners();

    }

    private void addListners() {

        view.getAdc().addItemListener(new AdcBoxListner());

        view.getLed().addItemListener(new LedBoxListner());

        view.getStraight().addActionListener(new ControlButtonListner(view.getStraight()));
        view.getBack().addActionListener(new ControlButtonListner(view.getBack()));
        view.getLeft().addActionListener(new ControlButtonListner(view.getLeft()));
        view.getRight().addActionListener(new ControlButtonListner(view.getRight()));
        view.getStop().addActionListener(new ControlButtonListner(view.getStop()));
        view.getRefresh().addActionListener(new ControlButtonListner(view.getRefresh()));
        view.getClearLog().addActionListener(new ControlButtonListner(view.getClearLog()));
        view.getLedPlus().addActionListener(new ControlButtonListner(view.getLedPlus()));
        view.getTest().addKeyListener(new ActionKeysListner());

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
    private class ActionKeysListner extends KeyAdapter {

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
    private class ControlButtonListner implements ActionListener {

        JButton button;

        private ControlButtonListner(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            switch (button.getText()) {
                case "Ref":
                    model.putPortToBox();
                    break;
                case "Clear":
                    model.clearLog();
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
    private class LedBoxListner implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {

            if ((e.getStateChange() == ItemEvent.SELECTED) | (e.getStateChange() == ItemEvent.DESELECTED)) {

                model.write("L");
            }

        }

    }

    private class AdcBoxListner implements ItemListener {

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
