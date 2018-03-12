

#include <nRF24L01.h>
#include <printf.h>
#include <RF24.h>
#include <RF24_config.h>
#include <SPI.h>



RF24 radio(A0, A1);
float const ADC1V1 = 0.02275;
float const ADC5V0 = 0.11556;
float battaryVoltage = 0.0;
float solarVoltage = 0.0;
char serialData[1];
int ledPin = 2;
 int counter = 0; 
char data1[5];



void setup() {
data1[0]='d';
data1[1]='1';
data1[2]='2';
data1[3]='3';
  pinMode(4, OUTPUT); // test
  pinMode(ledPin, OUTPUT);
  Serial.begin(76800);
  nrfSetup();
  serialData[0] = 'G';
  radio.write(serialData, 1);
  radio.startListening();
  
}
void loop() {
Serial.print(sizeof(data1));
delay(2);
Serial.print(data1);
delay(2000);
/*  
  if (radio.available()) {
    counter++;
   // Serial.println("Some data redy");
    char len = radio.getDynamicPayloadSize();
    radio.read(data1, len);
   // Serial.println(data1);
 //  if(counter==1){
//    battaryVoltage = (float) atoi(data1)*ADC1V1;
 //  }else{
//    solarVoltage = (float) atoi(data1)*ADC1V1;
//   counter=0;
   }
    
    
  }
*/

}
/*  
void serialEvent() {
  bool status;
  radio.stopListening();
  serialData[0] = Serial.read();
  status = radio.write(serialData, 1);
  if(serialData[0]=='G'){radio.startListening();
  Serial.print(battaryVoltage);
  delay(10);
  Serial.print(solarVoltage);
  }
 // Serial.println(status);

  
  delay(1);


}
*/


void nrfSetup() {
  bool master = true;
  //byte addresses[][6] = {"1Node","2Node"};
  byte addr1[] = {0xC2, 0xC2, 0xC2, 0xC2, 0xC2};
  byte addr2[] = {0xE7, 0xE7, 0xE7, 0xE7, 0xE7};

  uint8_t cnt;
  radio.begin();
  radio.txDelay = 160;
  radio.enableDynamicPayloads();
  radio.setCRCLength( RF24_CRC_16 ) ;
  radio.setChannel(4);  // Channel number - 0-125
  radio.setDataRate(RF24_1MBPS);
  radio.setPALevel(2);
  radio.setRetries(5, 5); // 1 - delay, 2 - amount
  radio.setAutoAck( true ) ;
  if (master)
  {
    radio.openWritingPipe(addr2);
    radio.openReadingPipe(1, addr1);
  }
  else
  {
    radio.openWritingPipe(addr1);
    radio.openReadingPipe(1, addr2);
  } ;

  radio.powerUp();
  blinc(100);
  radio.startListening();
  blinc(10);
  radio.stopListening();
}

void blinc(int ms) {

  digitalWrite(ledPin, HIGH);   // sets the LED on
  delay(ms);                  // waits for a second
  digitalWrite(ledPin, LOW);    // sets the LED off
  delay(ms);
  char serialData[0];
  sizeof(serialData);
  String
}





