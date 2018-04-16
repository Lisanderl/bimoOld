#include <Arduino.h>
#include <BimoControl.h>
#include <Motor.h>
#include <Ultrasonic.h>
#include <BimoSettings.h>
#include <ArduinoJson.h>
using namespace std;

BimoControl::BimoControl(Motor m1, Motor m2, Ultrasonic ultrasonic, int ledPin, int tonePin)
 :m_m1(m1), m_m2(m2), m_ultrasonic(ultrasonic), m_ledPin(ledPin), m_tonePin(tonePin)
{
	//int m1pwm, int m11, int m12, int m2pwm, int m21, int m22
//BimoControl(10, 4, 3, 9, 7, 8);

}

void BimoControl::findAndGetDataFromArray(char data[]){
StaticJsonBuffer<16> jsonBuffer;
JsonObject& root = jsonBuffer.parseObject(data);
if(root.containsKey(ACTION)){
    doAction(root.get<int>(ACTION));
    return;
}

if(root.containsKey(SPEED)){
m_settings.leftMotorPWM = root.get<int>(SPEED);
m_settings.rightMotorPWM = root.get<int>(SPEED);
return;
}

if(root.containsKey(ECHO)){
   if(root.is<bool>(ECHO)){
   m_settings.echoActive = root.get<bool>(ECHO);
  }
   if(root.is<int>(ECHO)){
   m_settings.echoDistance = root.get<int>(ECHO);
  }
return;
}

if(root.containsKey(LIGHT)){
       m_settings.lightPWM = root.get<int>(LIGHT);
       ledON();
return;
}
m_settings.isConnected = true;
}

void BimoControl::doAction(int val){

  switch (val) {
    case 1:   goStraight();
      break;
    case 2:   goBack();
      break;
    case 4:   goLeft();
      break;
    case 7:   goRight();
      break;
    case 5:   goLeftEasy();
      break;
    case 8:   goRightEasy();
      break;
    case 20:   alarm();
      break;
    default :  stopMove();
  }
}

void BimoControl::setMotorPWM(float divider){

	m_m1.setPWM((int)m_settings.leftMotorPWM*divider);
	m_m2.setPWM((int)m_settings.rightMotorPWM*divider);
}

void BimoControl::goStraight(){

    setMotorPWM(1.0);
	m_m1.on();
	m_m2.on();
}

void BimoControl::goBack(){

    setMotorPWM(0.8);
	m_m1.reverse();
	m_m2.reverse();
}

void BimoControl::goRight(){

    setMotorPWM(1.35);
	m_m2.reverse();
	m_m1.on();
}

void BimoControl::goRightEasy(){

    m_m2.setPWM(m_settings.rightMotorPWM/5);
	m_m1.on();
    m_m2.on();
}

void BimoControl::goLeft(){

    setMotorPWM(1.35);
	m_m2.on();
	m_m1.reverse();
}

void BimoControl::goLeftEasy(){

    m_m1.setPWM(m_settings.leftMotorPWM/5);
	m_m1.on();
    m_m2.on();
}

void BimoControl::stopMove(){

	m_m1.off();
	m_m2.off();
	m_m1.setPWM(0);
    m_m2.setPWM(0);
}

void BimoControl::alarm(){
    tone(m_tonePin, random(10, 900), 120);
}

void BimoControl::ledON() {
  analogWrite(m_ledPin, m_settings.lightPWM);
}

int BimoControl::measureEchoValue() {
  return (int)m_ultrasonic.Ranging(CM);
}

bool BimoControl::isMoving(){
 return ((m_m1.isWorkingRightNow()) or (m_m2.isWorkingRightNow())) ? 1 : 0;
}

void BimoControl::checkMovePermission() {
  
  if(!m_settings.isConnected || 
  (m_settings.echoActive && (measureEchoValue() < m_settings.echoDistance))
  ){
    stopMove();
    delay(10);
  }
}

void BimoControl::tryComeBack() {
 int pmw1 = m_settings.leftMotorPWM;
 int pmw2 = m_settings.rightMotorPWM;

 m_settings.leftMotorPWM = 110;
 m_settings.rightMotorPWM = 110;

 goLeft();
 delay(1100);
 stopMove();
 for(int i = 4; i != 0; i--){
 if(measureEchoValue() >= 8){
    goStraight();
     delay(800);
     stopMove();
     delay(200);
   }
  }
 m_settings.leftMotorPWM = pmw1;
 m_settings.rightMotorPWM = pmw2;
}


