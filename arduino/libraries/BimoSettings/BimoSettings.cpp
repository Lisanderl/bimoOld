
#include <BimoSettings.h>
#include <ArduinoJson.h>
using namespace std;

BimoSettings::BimoSettings(){
}

void BimoSettings::findAndGetDataFromArray(char* data){
StaticJsonBuffer<16> jsonBuffer;
JsonObject& root = jsonBuffer.parseObject(data);

if(root.containsKey(SPEED)){
leftMotorPWM = root.get<int>(SPEED);
rightMotorPWM = root.get<int>(SPEED);
return;
}

if(root.containsKey(ECHO)){
    if(root.is<bool>(ECHO)){
       echoDistance = root.get<bool>(ECHO);
    }

    if(root.is<int>(ECHO)){
       echoActive = root.get<int>(ECHO);
    }
return;
}

if(root.containsKey(LIGHT)){
    if(root.is<bool>(LIGHT)){
       lightTurnedOn = root.get<bool>(LIGHT);
    }

    if(root.is<int>(LIGHT)){
       lightPWM = root.get<int>(LIGHT);
    }
return;
}

if(root.containsKey(VOLTAGE)){
sendVoltage = root.get<bool>(VOLTAGE);
}

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




