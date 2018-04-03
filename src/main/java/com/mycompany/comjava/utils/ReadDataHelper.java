package com.mycompany.comjava.utils;

import com.mycompany.comjava.config.PropertyAction;

import java.nio.charset.Charset;

public class ReadDataHelper {

   private static int ACCURACY = 1023;
   private static double ADC5V0 = 5.0/ACCURACY;
   private static double ADC3V3 = 3.3/ACCURACY;
   private static double ADC1V1 = 1.1/ACCURACY;
   private static double EXPERIMENTAL_VALUE = 0.029348;

    public static String bytesToString(byte[] bytes){
        return new String(bytes, Charset.defaultCharset());
    }

    public static int getIntFromStr(String data){
          try{
              return  Integer.valueOf(data.replaceAll("[^0-9]", ""));
        }catch (NullPointerException ex){

        }
        return 0;
    }

    public static double getVoltage(int value) {
        return EXPERIMENTAL_VALUE*value;
    }
}
