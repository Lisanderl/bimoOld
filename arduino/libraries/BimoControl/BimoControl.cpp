#include "Arduino.h"
#include "BimoControl.h"
#include "Motor.h"

using namespace std;

BimoControl::BimoControl(Motor m1, Motor m2)
 :m_m1(m1), m_m2(m2)
{
	//int m1pwm, int m11, int m12, int m2pwm, int m21, int m22
//MashineControl(10, 4, 3, 9, 7, 8);

}	

void BimoControl::setMotorPWM(int pwm1, int pwm2){
	m_m1.setPWM(pwm1);
	m_m2.setPWM(pwm2);
}

void BimoControl::goStraight(){
	m_m1.on();
	m_m2.on();	
}
void BimoControl::goBack(){
	m_m1.reverse();
	m_m2.reverse();
}
void BimoControl::goRight(){
	m_m2.on();
	m_m1.reverse();	
}
void BimoControl::goRightEasy(){
	
	m_m1.reverse();	
}
void BimoControl::goLeft(){
	m_m2.reverse();	
	m_m1.on();	
}

void BimoControl::goLeftEasy(){
	m_m2.reverse();	
	
}

void BimoControl::stopMove(){
	m_m1.off();
	m_m2.off();
}

void BimoControl::blinc(int ms){
digitalWrite(m_ledPin, HIGH);
delay(ms);
digitalWrite(m_ledPin, LOW);
delay(ms);
}
	
void BimoControl::ledON(int pin, int mode) {
  
  analogWrite(pin, ledOutConst * mode);
}

void BimoControl::setBlincPin(int ledPin){
pinMode(ledPin, OUTPUT);
m_ledPin=ledPin;
}

