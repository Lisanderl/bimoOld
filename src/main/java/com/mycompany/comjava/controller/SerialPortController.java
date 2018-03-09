package com.mycompany.comjava.controller;

import com.mycompany.comjava.VoltageCalculation;
import com.mycompany.comjava.gui.MainFrame;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.comjava.logger.TextComponentLogger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class SerialPortController implements VoltageCalculation {

    @Autowired
    private TextComponentLogger logger;
    private SerialPort mainPort;
    private String portName;

    private int portSpeed, bits, stopBits, parity;
    private final MainFrame viev;

    public SerialPortController(MainFrame viev) {
        this.viev = viev;
        this.mainPort = null;
        putPortToBox();
    }
    
    private void setDefaultValues(){
        mainPort = null;
        portName = "COM3";
        portSpeed = 76800;
        bits = SerialPort.DATABITS_8;
        stopBits = SerialPort.STOPBITS_1;
        parity = SerialPort.PARITY_NONE;
    }

    private SerialPort createNewPort() {
        SerialPort port = null;

        if (mainPort == null || !mainPort.isOpened()) {
            try {
                port = new SerialPort(portName);
                port.openPort();
                port.setParams(portSpeed, bits, stopBits, parity);
                port.setEventsMask(SerialPort.MASK_RXCHAR);
                port.addEventListener(new MySerialEventListener());
                
            } catch (SerialPortException ex) {
                Logger.getLogger(SerialPortController.class.getName()).log(Level.SEVERE, null, ex);
                logger.ERROR(" Port is not creating ");
            }
        }

        return port;
    }

    public void write(String data) {

        try {

            mainPort.writeBytes(data.getBytes());
            logger.INFO(data + " sent");

        } catch (SerialPortException ex) {
            logger.ERROR(data + " Did not send, some exception");
        }

    }

    // request - data request
    // answ
    public void readRequest(String request) {

        try {
            mainPort.writeBytes(request.getBytes());

        } catch (SerialPortException ex) {
            Logger.getLogger(SerialPortController.class.getName()).log(Level.SEVERE, null, ex);
            logger.ERROR(" Read err ");
        }

    }

    public void putPortToBox() {
        viev.getCOMPort().removeAllItems();
        Optional<String[]> portNamesOptional = Optional.of(SerialPortList.getPortNames());
        if (portNamesOptional.isPresent()){
            Arrays.stream(portNamesOptional.get()).forEach(port ->  viev.getCOMPort().addItem(port));
        }else logger.ERROR("Port not found");
    }

//<editor-fold defaultstate="collapsed" desc="get set">
    public String getPortName() {
        return portName;

    }

    public void setPortName(String portName) {

        this.portName = portName;
        logger.INFO(" Port is changed to " + portName);

    }

    public int getPortSpeed() {
        return portSpeed;

    }

    public void setPortSpeed(int portSpeed) {
        this.portSpeed = portSpeed;
        logger.INFO(" Speed is changed to " + portSpeed);
        mainPort = null;
        mainPort = createNewPort();
    }

    public int getBits() {
        return bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
        logger.INFO(" Bits quantity changed to " + bits);

    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
        logger.INFO(" Stop bits changed to " + stopBits);
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
        logger.INFO(" Parity changed to " + parity);
    }

//</editor-fold>

    @Override
    public double getVoltage(double referanceVoltage, int value) {
        return (double)value*referanceVoltage;
    }
    
    
    private class MySerialEventListener implements SerialPortEventListener{
      int count=0;
     
        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()) {
                int bufferSize = event.getEventValue();
                try {
                    byte[] buffer = mainPort.readBytes(bufferSize);

                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
}
