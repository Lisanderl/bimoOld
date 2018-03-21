#ifndef BimoControl_h // если библиотека Button не подключена
#define BimoControl_h // тогда подключаем ее
#include "Arduino.h"
#include "Motor.h"
#include <Ultrasonic.h>
#include <BimoSettings.h>
class BimoControl {
	
public:
BimoControl(Motor m1, Motor m2, Ultrasonic ultrasonic, BimoSettings settings);
void setMotorPWM(int pwm1, int pwm2);
void setLedPWM(int val);
void goStraight();
void goBack();
void goRight();
void goLeft();
void stopMove();
//pls, if You use this functions, You should use HIGH pwm value (200 and more);
void goRightEasy();
void goLeftEasy();
//
void setBlincPin(int m_ledPin);
void blinc(int ms);
//pls use only analog pins, mode 0-5 = 0-100 % brightness
void ledON(int pin);

private:
Motor m_m1;
Motor m_m2;
Ultrasonic m_ultrasonic;
BimoSettings m_settings;
int m_ledPin;
int ledPmwValue;
};

#endif