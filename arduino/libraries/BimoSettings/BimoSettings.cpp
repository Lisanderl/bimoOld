#include "Arduino.h"
#include "BimoSettings.h"
#include "Motor.h"
#include <Ultrasonic.h>
using namespace std;

BimoSettings::BimoSettings(){
}

void BimoSettings::setLeftMotorPWM(int val){
 leftMotorPWM=val;
}

void BimoSettings::setRightMotorPWM(int val){
 rightMotorPWM=val;
}

void BimoSettings::setLightPWM(int val){
lightPWM=val;
}

void BimoSettings::setEchoDistance(int val){
echoDistance=val;
}

void BimoSettings::setSendVoltage(bool val){
sendVoltage=val;
}

void BimoSettings::setEchoActive(bool val){
echoActive=val;
}

void BimoSettings::setLightTurnedOn(bool val){
lightTurnedOn=val;
}

int BimoSettings::getLeftMotorPWM(){
return leftMotorPWM;
}

int BimoSettings::getRightMotorPWM(){
return rightMotorPWM;
}

int BimoSettings::getLightPWM(){
return lightPWM;
}

int BimoSettings::getEchoDistance(){
return echoDistance;
}

bool BimoSettings::isSendVoltage(){
return sendVoltage;
}

bool BimoSettings::isEchoActive(){
return echoActive;
}

bool BimoSettings::isLightTurnedOn(){
return lightTurnedOn;
}




