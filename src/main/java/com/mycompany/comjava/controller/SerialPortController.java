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
public class SerialPortController {

    private MainFrame mainFrame;
    private TextComponentLogger logger;
    private SerialPort mainPort;
    private String portName;

    private int portSpeed, bits, stopBits, parity;

    @Autowired
    public SerialPortController(MainFrame mainFrame) {

        this.mainFrame = mainFrame;
        this.logger = TextComponentLogger.getInstance(mainFrame.getLog());
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

    private SerialPort createNewPortAndOpen() {
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
                logger.ERROR(" Port has not created ");
            }
        }

        return port;
    }

    public synchronized void write(String data) {

        try {
            mainPort.writeBytes(data.getBytes());
            logger.INFO(data + " sent");

        } catch (NullPointerException|SerialPortException ex) {
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
        mainFrame.getCOMPort().removeAllItems();
        Optional<String[]> portNamesOptional = Optional.of(SerialPortList.getPortNames());
        if (portNamesOptional.isPresent()){
            portName = portNamesOptional.get()[0];
            Arrays.stream(portNamesOptional.get()).forEach(port ->  mainFrame.getCOMPort().addItem(port));
        }else {
            logger.ERROR("Port not found");
            mainFrame.getCOMPort().addItem("none");}
    }

//<editor-fold defaultstate="collapsed" desc="get set">
    public String getPortName() {
        return portName;
    }

    private void resetPort(){
        if(mainPort!=null) {
            try {
                mainPort.closePort();
            } catch (SerialPortException e) {
                e.printStackTrace();
                logger.ERROR("Port not closed");
            }
        }
        mainPort = null;
        mainPort = createNewPortAndOpen();
    }

    public void setPortName(String portName) {
        this.portName = portName;
        resetPort();
        logger.INFO(" Port is changed to " + portName);
    }

    public void setPortSpeed(int portSpeed) {
        this.portSpeed = portSpeed;
        resetPort();
        logger.INFO(" Speed is changed to " + portSpeed);
    }

    public void setBits(int bits) {
        this.bits = bits;
        resetPort();
        logger.INFO(" Bits quantity changed to " + bits);
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
        resetPort();
        logger.INFO(" Stop bits changed to " + stopBits);
    }

    public void setParity(int parity) {
        this.parity = parity;
        resetPort();
        logger.INFO(" Parity changed to " + parity);
    }

//</editor-fold>

    
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
