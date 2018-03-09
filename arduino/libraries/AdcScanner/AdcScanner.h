#ifndef AdcScanner_h // если библиотека Button не подключена
#define AdcScanner_h // тогда подключаем ее
#include "Arduino.h"

class AdcScanner {
public:
 AdcScanner(int analogRefValue);
 int scan(int portName, int measurements);
 float voltageCalculate(int portName, float voltDivider);

private:
float referenceCnst; 

};

#endif