#ifndef BimoSettings_h // если библиотека Button не подключена
#define BimoSettings_h // тогда подключаем ее
class BimoSettings {
	
public:
BimoSettings();

int leftMotorPWM = 100;
int rightMotorPWM = 100;
int lightPWM;
int echoDistance;
bool isConnected;
bool sendVoltage;
bool echoActive;
long connectionCount = 0;
long voltageCount = 0;
private:

};

#endif
