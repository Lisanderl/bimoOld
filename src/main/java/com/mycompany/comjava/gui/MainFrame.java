/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comjava.gui;

import com.mycompany.comjava.SerialPortModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jssc.SerialPort;

/**
 *
 * @author oleg
 */
public class MainFrame extends JFrame {

    private JLabel configL, speedL, bitsL, stopBitsL;
    private JPanel mainPanel, topPanel, bottomPanel, leftPanel, rightPanel, bottomLeft;
    private JButton straight, back, left, right, test, stop, refresh, clearLog, ledPlus;
    private JCheckBox led, adc;
    private JTextArea log;
    private JScrollPane scrool;
    private JComboBox<String> COMPort;
    private JComboBox<Integer> portSpeed, bits, stopBits, parity;
    private JTextField solarVoltage, battaryVoltage;
    private final SerialPortModel model;

    public MainFrame() throws HeadlessException {
        init();
        model = new SerialPortModel(this);
        super.setTitle("Bimo user interface");
        super.setMinimumSize(new Dimension(500, 400));
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        addComponents();
        log.setEnabled(false);
        super.setVisible(true);
    }

    private void init() {

        //labels
        configL = new JLabel("Port ");
        speedL = new JLabel("Speed");
        bitsL = new JLabel("bits");
        stopBitsL = new JLabel("stop");
        //panels
        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new FlowLayout());
        bottomPanel = new JPanel(new FlowLayout());
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new GridBagLayout());
        bottomLeft = new JPanel(new FlowLayout());
        //buttons
        straight = new JButton("W");
        back = new JButton("S");
        left = new JButton("A");
        right = new JButton("D");
        test = new JButton("test");
        stop = new JButton("C");
        refresh = new JButton("Ref");
        clearLog = new JButton("Clear");
        ledPlus = new JButton("Led+");
        //comboboxes...
        COMPort = new JComboBox<String>();
        portSpeed = new JComboBox<Integer>();
        bits = new JComboBox<Integer>();
        stopBits = new JComboBox<Integer>();
        parity = new JComboBox<Integer>();
        //other
        log = new JTextArea("Hi, please push the 'test' button, \n if You wish control with keyboard ");
        scrool = new JScrollPane(log);
        adc = new JCheckBox("ADC");
        led = new JCheckBox("Led");

        solarVoltage = new JTextField(7);
        battaryVoltage = new JTextField(7);

    }

    private void addComponents() {
        super.getContentPane().add(mainPanel);
        GridBagConstraints c = new GridBagConstraints();

        // main panel..
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        //toppanel ....
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.add(configL);
        topPanel.add(COMPort);
        topPanel.add(refresh);
        topPanel.add(speedL);
        topPanel.add(portSpeed);
        topPanel.add(bitsL);
        topPanel.add(bits);
        topPanel.add(stopBitsL);
        topPanel.add(stopBits);
        topPanel.add(parity);

        //bottomPanel...
        bottomPanel.add(leftPanel);
        bottomPanel.add(rightPanel);
        System.err.println(getWidth() + " " + getHeight());
        //left
        leftPanel.setPreferredSize(new Dimension(getWidth() / 2 + 100, getHeight() - 80));
        leftPanel.setBackground(Color.lightGray);
        scrool.setPreferredSize(new Dimension(430, 150));
        leftPanel.add(new JPanel(), BorderLayout.NORTH);
        leftPanel.add(scrool, BorderLayout.CENTER);
        leftPanel.add(bottomLeft, BorderLayout.SOUTH);
        bottomLeft.add(straight);
        bottomLeft.add(left);
        bottomLeft.add(back);
        bottomLeft.add(right);
        bottomLeft.add(stop);
        bottomLeft.add(test);

        //right
        rightPanel.setPreferredSize(new Dimension(getWidth() / 2 - 130, getHeight() - 80));

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 5;
        c.gridy = 1;
        rightPanel.add(solarVoltage, c);
        solarVoltage.setEditable(false);
        c.gridx = 6;
        rightPanel.add(battaryVoltage, c);
        battaryVoltage.setEditable(false);
        // rightPanel.add(adc, c);
        c.gridx = 6;
        c.gridy = 2;
        // rightPanel.add(led, c);
        rightPanel.add(adc, c);
        c.gridx = 5;
        rightPanel.add(ledPlus, c);

        c.gridx = 5;
        c.gridy = 3;
        rightPanel.add(clearLog, c);

   
        //combo
        comboBoxConfig();

    }

    //<editor-fold defaultstate="collapsed" desc="comboboxes data">
    private void comboBoxConfig() {

        portSpeed.addItem(SerialPort.BAUDRATE_9600);
        portSpeed.addItem(SerialPort.BAUDRATE_14400);
        portSpeed.addItem(SerialPort.BAUDRATE_19200);
        portSpeed.addItem(SerialPort.BAUDRATE_38400);
        portSpeed.addItem(SerialPort.BAUDRATE_57600);
        portSpeed.addItem(76800);
        portSpeed.addItem(SerialPort.BAUDRATE_115200);
        portSpeed.addItem(SerialPort.BAUDRATE_128000);

        bits.addItem(SerialPort.DATABITS_8);
        bits.addItem(SerialPort.DATABITS_7);
        bits.addItem(SerialPort.DATABITS_6);
        bits.addItem(SerialPort.DATABITS_5);

        stopBits.addItem(SerialPort.STOPBITS_1);
        stopBits.addItem(SerialPort.STOPBITS_1_5);
        stopBits.addItem(SerialPort.STOPBITS_2);

        parity.addItem(SerialPort.PARITY_NONE);
        parity.addItem(SerialPort.PARITY_ODD);

//</editor-fold>
    }

//<editor-fold defaultstate="collapsed" desc="get_set">
    public JCheckBox getAdc() {
        return adc;
    }

    public JButton getClearLog() {
        return clearLog;
    }

    public SerialPortModel getModel() {
        return model;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JLabel getStopBitsL() {
        return stopBitsL;
    }

    public JButton getStop() {
        return stop;
    }

    public JButton getStraight() {
        return straight;
    }

    public JButton getBack() {
        return back;
    }

    public JButton getLeft() {
        return left;
    }

    public JButton getRight() {
        return right;
    }

    public JButton getTest() {
        return test;
    }

    public JButton getRefresh() {
        return refresh;
    }

    public JTextArea getLog() {
        return log;
    }


    public JComboBox<String> getCOMPort() {
        return COMPort;
    }

    public JComboBox<Integer> getPortSpeed() {
        return portSpeed;
    }

    public JComboBox<Integer> getBits() {
        return bits;
    }

    public JComboBox<Integer> getStopBits() {
        return stopBits;
    }

    public JComboBox<Integer> getParity() {
        return parity;
    }

    public JTextField getSolarVoltage() {
        return solarVoltage;
    }

    public JTextField getBattaryVoltage() {
        return battaryVoltage;
    }

    public JCheckBox getLed() {
        return led;
    }

    public JButton getLedPlus() {
        return ledPlus;
    }

//</editor-fold>
}
