#ifndef BimoSettings_h // если библиотека Button не подключена
#define BimoSettings_h // тогда подключаем ее
class BimoSettings {
	
public:
BimoSettings();

int leftMotorPWM = 50;
int rightMotorPWM = 50;
int lightPWM = 0;
int echoDistance = 10;
bool isConnected = true;
bool echoActive = false;
long count = 0;
private:
};

#endif
