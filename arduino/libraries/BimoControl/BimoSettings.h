#ifndef BimoSettings_h // если библиотека Button не подключена
#define BimoSettings_h // тогда подключаем ее
class BimoSettings {
	
public:
BimoSettings();

int const echoConst = 5000;
int const connectionConst = 75000;

int leftMotorPWM = 50;
int rightMotorPWM = 50;
int lightPWM = 0;


bool echoActive = false;
bool echoStart = false;
int echoDistance = 10;
long echoCount = 0;

bool isConnected = true;
long connectionCount = 0;
private:
};

#endif
