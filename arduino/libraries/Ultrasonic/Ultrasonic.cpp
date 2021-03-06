/*
  Ultrasonic.cpp - Library for HC-SR04 Ultrasonic Ranging Module.library

  Created by ITead studio. Apr 20, 2010.
  iteadstudio.com
  
  updated by noonv. Feb, 2011
  http://robocraft.ru
*/

#include "Ultrasonic.h"

Ultrasonic::Ultrasonic(int TP, int EP)
{
   pinMode(TP, OUTPUT);
   pinMode(EP, INPUT);
   Trig_pin=TP;
   Echo_pin=EP;
}

long Ultrasonic::Timing()
{
  digitalWrite(Trig_pin, LOW);
  delayMicroseconds(2);
  digitalWrite(Trig_pin, HIGH);
  delayMicroseconds(10);
  digitalWrite(Trig_pin, LOW);
  duration = pulseIn(Echo_pin, HIGH);
  return duration;
}

float Ultrasonic::Ranging(int sys)
{
  Timing();
  distacne_cm = (float)duration / 29.0 / 2.0 ;
  distance_inc = (float)duration / 74.0 / 2.0;
  if (sys)
    return distacne_cm;
  else
    return distance_inc;
}
