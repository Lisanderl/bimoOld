#include "Arduino.h"
#include "AdcScanner.h"



AdcScanner::AdcScanner(int analogRefValue){
	analogReference(analogRefValue);
switch(analogRefValue){
	case 1 : referenceCnst = 0.004883; analogReference(DEFAULT); break;
	case 3 : referenceCnst = 0.001075; analogReference(INTERNAL); break;
	case 0 : referenceCnst = analogRefValue/1024; analogReference(EXTERNAL); break;
	

}	
}

int AdcScanner::scan(int portName, int measurements){
	
long measuringValue=0;
for(int i = 0; i<measurements; i++){
measuringValue+=analogRead(portName);
 delayMicroseconds(900); 
  }	
return measuringValue/measurements;
	

}

float AdcScanner::voltageCalculate(int portName, float voltDivider){
	float voltage = (float) adcScan(portName, 10);
	return voltage * referenceCnst * voltDivider;
	
}