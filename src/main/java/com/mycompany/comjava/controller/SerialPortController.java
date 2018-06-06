package com.mycompany.comjava.controller;

import com.mycompany.comjava.config.PropertyAction;
import com.mycompany.comjava.gui.MainFrame;

import java.util.Arrays;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mycompany.comjava.utils.ReadDataHelper.*;
import com.mycompany.comjava.utils.TextComponentLogger;
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
        putPortNameToBox();
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

    public void putPortNameToBox() {

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
                logger.ERROR("Port has not closed");
            }
        }
        mainPort = null;
        mainPort = createNewPortAndOpen();
    }

    public void setPortName(String portName) {
        this.portName = portName;
        resetPort();
        logger.INFO(" Port has changed to " + portName);
    }

    public void setPortSpeed(int portSpeed) {
        this.portSpeed = portSpeed;
        resetPort();
        logger.INFO(" Speed has changed to " + portSpeed);
    }

    public void setBits(int bits) {
        this.bits = bits;
        resetPort();
        logger.INFO(" Bits quantity has changed to " + bits);
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
        resetPort();
        logger.INFO(" Stop bits has changed to " + stopBits);
    }

    public void setParity(int parity) {
        this.parity = parity;
        resetPort();
        logger.INFO(" Parity has changed to " + parity);
    }

//</editor-fold>

    private class MySerialEventListener implements SerialPortEventListener {
        private Timer timer;
        private final int delay = 16500;
        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()) {
                int bufferSize = event.getEventValue();
                try {
                    String data = bytesToString(mainPort.readBytes(bufferSize));
                    System.out.println(data);
                    String key = PropertyAction.isStrContainsShortValue(data);
                    System.out.println(key);
                    if(key.isEmpty()){logger.ERROR("Read empty str ");
                    }else {
                        switch (key){
                            case "V" :
                                mainFrame.getBatteryVoltage().setText(String.valueOf(getVoltage(getIntFromStr(data))));
                                write(PropertyAction.jsonValue(PropertyAction.CONNECT, true));
                                mainFrame.getConnection().setText("Connected");
                                timer = new Timer();
                                timer.schedule(new ConnectionTask(), delay);
                            break;
                        }
                    }
                } catch (SerialPortException e) {
                    e.printStackTrace();
                    logger.ERROR("read error");
                }
            }
        }
        
    }

    private class ConnectionTask extends TimerTask{

        @Override
        public void run() {
            mainFrame.getConnection().setText("No connect");
        }
    }


}
