#ifndef BimoControl_h // если библиотека Button не подключена
#define BimoControl_h // тогда подключаем ее
#include <Motor.h>
#include <Ultrasonic.h>
#include <BimoSettings.h>
#include <ArduinoJson.h>
class BimoControl {
	
public:
BimoControl(Motor m1, Motor m2, Ultrasonic ultrasonic, int ledPin, int tonePin);

String LIGHT = "L";
String SPEED = "S";
String ECHO = "E";
String VOLTAGE = "V";
String ACTION = "A";
String CONNECT = "C";
Ultrasonic m_ultrasonic;
BimoSettings m_settings;

void findAndGetDataFromArray(char data[]);
void setMotorPWM();
void goStraight();
void goBack();
void goRight();
void goLeft();
void stopMove();
//pls, if You use this functions, You should use HIGH pwm value (200 and more);
void goRightEasy();
void goLeftEasy();
void ledON();
void alarm();
int measureEchoValue();
void tryComeBack();
bool isMoving();
void checkMovePermission();
private:

void doAction(int val);
Motor m_m1;
Motor m_m2;
int m_ledPin;
int m_tonePin;
//values vor sos
int pmw1;
int pmw2;
};

#endif
