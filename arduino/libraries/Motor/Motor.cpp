#include "Arduino.h"
#include "Motor.h"


using namespace std;

Motor::Motor(int PWM, int positive, int negative) 
 :m_PWM(PWM), m_Positive(positive), m_Negative(negative)
{
//MashineControl(10, 4, 3, 9, 7, 8);

//set up pins
  pinMode(m_PWM, OUTPUT);
  pinMode(m_Positive , OUTPUT);
  pinMode(m_Negative, OUTPUT);
}	

void Motor::setPWM(int pwmValue) //: m_pwmValue(pwmValue)
{
	m_pwmValue=pwmValue;
  //analogWrite(m1PWM, m1);
}

 int Motor::getPWM()
 {
  return m_pwmValue;
 }

void Motor::on() {
  analogWrite(m_PWM, m_pwmValue);
  digitalWrite(m_Positive, HIGH);
  digitalWrite(m_Negative, LOW);
}
void Motor::reverse() {
 analogWrite(m_PWM, m_pwmValue);
  digitalWrite(m_Positive, LOW);
  digitalWrite(m_Negative, HIGH);
}
void Motor::off() {
 analogWrite(m_PWM, 0);
  digitalWrite(m_Positive, LOW);
  digitalWrite(m_Negative, LOW);
}

	




