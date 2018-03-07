package com.mycompany.comjava;

import com.mycompany.comjava.gui.MainFrame;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;

public final class SerialPortModel implements VoltageCalculation{

    private static SerialPort mainPort;
    private String portName;

    private int portSpeed, bits, stopBits, parity;
    private final MainFrame viev;

    public SerialPortModel(MainFrame viev) {
        this.viev = viev;
        this.mainPort = null;
        putPortToBox();
        //set defaut port config
        mainPort = null;
        portName = "COM3";
        portSpeed = 76800;
        bits = SerialPort.DATABITS_8;
        stopBits = SerialPort.STOPBITS_1;
        parity = SerialPort.PARITY_NONE;

    }

    private SerialPort createPort() {
        SerialPort port = null;

        if (mainPort == null || !mainPort.isOpened()) {
            try {
                port = new SerialPort(portName);
                
                port.openPort();
                port.setParams(portSpeed, bits, stopBits, parity);
                port.setEventsMask(SerialPort.MASK_RXCHAR);
                port.addEventListener(new MySerialEventListner());
                
            } catch (SerialPortException ex) {
                Logger.getLogger(SerialPortModel.class.getName()).log(Level.SEVERE, null, ex);
                writeToLog(" Port is not creating ");
            }
        }

        return port;
    }

    public void write(String data) {

        try {

            mainPort.writeBytes(data.getBytes());
            writeToLog(data + " sent");

        } catch (SerialPortException ex) {
            writeToLog(data + " not sended, some exception");
        }

    }

    // request - data request
    // answ
    public void readRequest(String request) {

        try {
            mainPort.writeBytes(request.getBytes());

        } catch (SerialPortException ex) {
            Logger.getLogger(SerialPortModel.class.getName()).log(Level.SEVERE, null, ex);
            writeToLog(" Read err ");
        }

    }

    public void writeToLog(String data) {
        viev.getLog().setText(viev.getLog().getText() + "\n" + data);

    }

    public void clearLog() {
        viev.getLog().setText("");

    }

    public void putPortToBox() {
        viev.getCOMPort().removeAllItems();
        for (String port : SerialPortList.getPortNames()) {
            viev.getCOMPort().addItem(port);
        }
    }

//<editor-fold defaultstate="collapsed" desc="get set">
    public String getPortName() {
        return portName;

    }

    public void setPortName(String portName) {

        this.portName = portName;
        writeToLog(" Port is changed to " + portName);

    }

    public int getPortSpeed() {
        return portSpeed;

    }

    public void setPortSpeed(int portSpeed) {
        this.portSpeed = portSpeed;
        writeToLog(" Speed is changed to " + portSpeed);
        mainPort = null;
        mainPort = createPort();
    }

    public int getBits() {
        return bits;
    }

    public void setBits(int bits) {
        this.bits = bits;
        writeToLog(" Bits quantity changed to " + bits);

    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
        writeToLog(" Stop bits changed to " + stopBits);
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
        writeToLog(" Parity changed to " + parity);
    }

//</editor-fold>

    @Override
    public double getVoltage(double referanceVoltage, int value) {
        return (double)value*referanceVoltage;
    }
    
    
    private class MySerialEventListner implements SerialPortEventListener{
      int count=0;
     
        @Override
        public void serialEvent(SerialPortEvent event) {
            count++;
            if(event.isRXCHAR()){
                
                try {
                    
                   if(count==1)  viev.getBattaryVoltage().setText(mainPort.readString(event.getEventValue())); 
                   if(count==2) { viev.getSolarVoltage().setText(mainPort.readString(event.getEventValue())); count=0; }
                } catch (SerialPortException ex) {
                    Logger.getLogger(SerialPortModel.class.getName()).log(Level.SEVERE, null, ex);
                }
}
        }
        
    }
}
