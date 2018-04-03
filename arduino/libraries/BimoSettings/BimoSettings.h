#ifndef BimoSettings_h // если библиотека Button не подключена
#define BimoSettings_h // тогда подключаем ее
class BimoSettings {
	
public:
BimoSettings();

int leftMotorPWM = 100;
int rightMotorPWM = 100;
int lightPWM = 0;
int echoDistance = 10;
int isConnected = 0;
int sendVoltage = 0;
int echoActive = 0;
long count = 0;
private:

};

#endif
