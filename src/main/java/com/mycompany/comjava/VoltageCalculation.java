/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.comjava;

public interface VoltageCalculation {

int ACCURACY = 1023;
double ADC5V0 = 5.0/ACCURACY;
double ADC3V3 = 3.3/ACCURACY;
double ADC1V1 = 1.1/ACCURACY;
    double getVoltage(double referanceVoltage, int value);
}
