#include "Arduino.h"
#include "BimoSettings.h"
#include "Motor.h"
#include <Ultrasonic.h>
using namespace std;

BimoSettings::BimoSettings()
{

void BimoSettings::setLeftMotorPWM(int val){

}


void BimoSettings::setLeftMotorPWM(int val){
 this.leftMotorPWM=val;
}
void BimoSettings::setRightMotorPWM(int val){
 this.rightMotorPWM=val;
}
void BimoSettings::setLightPWM(int val){
this.lightPWM=val;
}
void BimoSettings::setEchoDistance(int val){
this.echoDistance=val;
}
void BimoSettings::setSendVoltage(bool val){
this.sendVoltage=val;
}
void BimoSettings::setEchoActive(bool val){
this.echoActive=val;
}
void BimoSettings::setLightTurnedOn(bool val){
this.lightTurnedOn=val;
}

int getLeftMotorPWM(){
return this.leftMotorPWM;
}
int getRightMotorPWM(){
return this.rightMotorPWM;
}
int getLightPWM(){
return this.lightPWM;
}
int getEchoDistance(){
return this.echoDistance;
}
bool isSendVoltage(){
return this.sendVoltage;
}
bool isEchoActive(){
return this.echoActive;
}
bool isLightTurnedOn(){
return this.lightTurnedOn;
}

}	


