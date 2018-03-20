#ifndef BimoSettings_h // если библиотека Button не подключена
#define BimoSettings_h // тогда подключаем ее
#include "Arduino.h"
#include <Ultrasonic.h>
class BimoSettings {
	
public:
BimoSettings();

String LIGHT = "L";
String SPEED = "S";
String ECHO = "E";
String VOLTAGE = "Vgit";


void setLeftMotorPWM(int val);
void setRightMotorPWM(int val);
void setLightPWM(int val);
void setEchoDistance(int val);
void setSendVoltage(bool val);
void setEchoActive(bool val);
void setLightTurnedOn(bool val);

int getLeftMotorPWM();
int getRightMotorPWM();
int getLightPWM();
int getEchoDistance();
bool isSendVoltage();
bool isEchoActive();
bool isLightTurnedOn();

private:
int leftMotorPWM;
int rightMotorPWM;
int lightPWM;
int echoDistance;
bool sendVoltage;
bool echoActive;
bool lightTurnedOn;
};

#endif