#include "Arduino.h"
#include "BimoControl.h"
#include "Motor.h"
#include <Ultrasonic.h>
#include <BimoSettings.h>
using namespace std;

BimoControl::BimoControl(Motor m1, Motor m2, Ultrasonic ultrasonic, BimoSettings settings)
 :m_m1(m1), m_m2(m2), m_ultrasonic(ultrasonic), m_settings(settings)
{
	//int m1pwm, int m11, int m12, int m2pwm, int m21, int m22
//MashineControl(10, 4, 3, 9, 7, 8);

}	

void BimoControl::setMotorPWM(){
	m_m1.setPWM(m_settings.getLeftMotorPWM());
	m_m2.setPWM(m_settings.getRightMotorPWM());
}

void BimoControl::goStraight(){
    setMotorPWM();
	m_m1.on();
	m_m2.on();	
}

void BimoControl::goBack(){
    setMotorPWM();
	m_m1.reverse();
	m_m2.reverse();
}
void BimoControl::goRight(){
    setMotorPWM();
	m_m2.on();
	m_m1.reverse();	
}
void BimoControl::goRightEasy(){
    m_m1.setPWM(m_settings.getLeftMotorPWM()/3);
	m_m1.on();
    m_m2.on();
}
void BimoControl::goLeft(){
    setMotorPWM();
	m_m2.reverse();	
	m_m1.on();	
}

void BimoControl::goLeftEasy(){
    m_m2.setPWM(m_settings.getRightMotorPWM()/3);
	m_m1.on();
    m_m2.on();
}

void BimoControl::stopMove(){
	m_m1.off();
	m_m2.off();
		m_m1.setPWM(0);
    	m_m2.setPWM(0);
}

void BimoControl::blinc(int ms){
digitalWrite(m_ledPin, HIGH);
delay(ms);
digitalWrite(m_ledPin, LOW);
delay(ms);
}
	
void BimoControl::ledON(int pin) {
  analogWrite(pin, m_settings.getLightPWM());
}

void BimoControl::setBlincPin(int ledPin){
pinMode(ledPin, OUTPUT);
m_ledPin=ledPin;

}
