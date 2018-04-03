#ifndef Motor_h // если библиотека Button не подключена
#define Motor_h // тогда подключаем ее
#include "Arduino.h"

class Motor {
public:
 Motor(int PWM, int positive, int negative);
 void setPWM(int pwm);
 int getPWM();
 void on();
 void off();
 void reverse();
 int isWorkingRightNow();
private:
int m_PWM;
int m_Positive;
int m_Negative;
int m_pwmValue;

};

#endif