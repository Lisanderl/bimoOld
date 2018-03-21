#ifndef BimoSettings_h // если библиотека Button не подключена
#define BimoSettings_h // тогда подключаем ее
#include <ArduinoJson.h>
class BimoSettings {
	
public:
BimoSettings();

String LIGHT = "L";
String SPEED = "S";
String ECHO = "E";
String VOLTAGE = "V";


void BimoSettings::findAndGetDataFromArray(char* data);

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