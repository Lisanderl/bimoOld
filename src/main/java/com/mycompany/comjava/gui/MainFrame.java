/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comjava.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.HeadlessException;

import javax.swing.*;

import jssc.SerialPort;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
public class MainFrame extends JFrame {

    private JLabel configL, speedL, bitsL, stopBitsL, connection, powerLabel;
    private JPanel mainPanel, topPanel, bottomPanel, leftPanel, rightPanel, bottomLeft;
    private JButton  test, refresh, clearLog, verify;
    private JCheckBox led, power, pwm, echo;
    private JTextArea log;
    private JScrollPane scroll;
    private JComboBox<String> COMPort;
    private JComboBox<Integer> portSpeed, bits, stopBits, parity, echoRange;
    private JTextField batteryVoltage;
    private JSlider speedSlider, lightSlider;

    public MainFrame() throws HeadlessException {
        init();
        super.setTitle("Bimo user interface");
        super.setMinimumSize(new Dimension(600, 500));
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
        connection = new JLabel("No connection");
        powerLabel = new JLabel("Power: ");
        //panels
        mainPanel = new JPanel(new BorderLayout());
        topPanel = new JPanel(new FlowLayout());
        bottomPanel = new JPanel(new FlowLayout());
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel(new GridBagLayout());
        bottomLeft = new JPanel(new FlowLayout());
        //buttons
        test = new JButton("Control with keyboard");
        refresh = new JButton("Ref");
        clearLog = new JButton("Clear");
        verify = new JButton("Verify");
        //comboboxes...
        COMPort = new JComboBox<>();
        portSpeed = new JComboBox<>();
        bits = new JComboBox<>();
        stopBits = new JComboBox<>();
        parity = new JComboBox<>();
        echoRange = new JComboBox<>();
        //other
        log = new JTextArea ("Hi, first change speedSlider and add other adjustments," +
                             " \n then push the 'test' button," +
                             " \n if You wish control with keyboard ");
        log.setDisabledTextColor(Color.BLACK);
        scroll = new JScrollPane(log);
        power = new JCheckBox("ADC");
        led = new JCheckBox("Led");
        pwm = new JCheckBox("Pwm");
        echo = new JCheckBox("<~~");

        batteryVoltage = new JTextField(7);

        //Sliders // magic numbers hello
        speedSlider = new JSlider( JSlider.VERTICAL, 90, 180, 90);
        lightSlider = new JSlider( JSlider.VERTICAL, 0, 250, 0);
        //config it
        speedSlider.setPaintLabels(true);
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setSnapToTicks(true);
        speedSlider.setEnabled(false);

        lightSlider.setPaintLabels(true);
        lightSlider.setMajorTickSpacing(25);
        lightSlider.setSnapToTicks(true);
        lightSlider.setEnabled(false);


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
        scroll.setPreferredSize(new Dimension(430, 150));
        leftPanel.add(new JPanel(), BorderLayout.NORTH);
        leftPanel.add(scroll, BorderLayout.CENTER);
        leftPanel.add(bottomLeft, BorderLayout.SOUTH);
        bottomLeft.add(test);
        bottomLeft.add(clearLog);
        bottomLeft.add(verify);

        //right
        rightPanel.setPreferredSize(new Dimension(getWidth() / 2 - 130, getHeight() - 80));

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridy = 0;
        c.gridx = 6;
        rightPanel.add(connection, c);
        c.gridy = 1;
        rightPanel.add(batteryVoltage, c);
        c.gridx = 5;
        rightPanel.add(powerLabel, c);
        c.gridy = 2;
        rightPanel.add(echo, c);
        c.gridx = 6;
        rightPanel.add(echoRange, c);
        batteryVoltage.setEditable(false);
        c.gridx = 6;
        c.gridy = 3;
        rightPanel.add(pwm, c);
        c.gridx = 5;
        rightPanel.add(led, c);
        c.gridy = 4;
        rightPanel.add(lightSlider, c);
        c.gridx = 6;
        rightPanel.add(speedSlider, c);
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
        for (int i = 5; i!=11; i++){
            echoRange.addItem(i);
        }


//</editor-fold>
    }


//</editor-fold>
}
