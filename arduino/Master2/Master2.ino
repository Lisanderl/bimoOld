#include <Arduino.h>
#include <RF24.h>
#include <RF24_config.h>
#include <SPI.h>
#include <nRF24L01.h>
#include <printf.h>

RF24 radio(A0, A1);
float const ADC1V1 = 0.02275;
float const ADC5V0 = 0.11556;
float battaryVoltage = 0.0;
int ledPin = 2;
int counter = 0;

void setup() {
  pinMode(4, OUTPUT); // test
  pinMode(ledPin, OUTPUT);
  Serial.begin(115200);
  nrfSetup();
  //  radio.write(serialData, 1);
  radio.startListening();

}
void loop() {
String data = "";
  if (radio.available()) {
    int len = radio.getDynamicPayloadSize();
    char recivedData[len]; 
    radio.read(recivedData, len);
  }
  
    while (Serial.available()) {
    data += (char)Serial.read();
    delay(1);

  }
      if(!Serial.available()){
      Serial.print(data);
    }
  
  delay(1500);
  Serial.println("opa");
  delay(1000);
  
}

void serialEvent() {
  
  //String dataa = "123321";
  //int len = dataa.length() + 1;
  //char strArray[len];
  //dataa.toCharArray(strArray, len);
  //delay(1);
  //Serial.print(strArray);
  // Serial.print(jsonParser("WriteToSlave", (String)requestStatus)); work
  //Serial.print(gg.length()); work
}

void blinc(int ms) {

  digitalWrite(ledPin, HIGH);   // sets the LED on
  delay(ms);                  // waits for a second
  digitalWrite(ledPin, LOW);    // sets the LED off
  delay(ms);

}

void nrfSetup() {
  bool master = true;
  byte addr1[] = {
    0xC2, 0xC2, 0xC2, 0xC2, 0xC2  };
  byte addr2[] = {
    0xE7, 0xE7, 0xE7, 0xE7, 0xE7  };

  radio.begin();
  radio.txDelay = 135;
  radio.enableDynamicPayloads();
  radio.setCRCLength( RF24_CRC_16 ) ;
  radio.setChannel(4);  // Channel number - 0-125
  radio.setDataRate(RF24_1MBPS);
  radio.setPALevel(2);
  radio.setRetries(1, 3); // 1 - delay, 2 - amount
  radio.setAutoAck( true ) ;
  radio.openWritingPipe(addr2);
  radio.openReadingPipe(1, addr1);
  radio.powerUp();
  blinc(100);
  radio.startListening();
  blinc(10);
  radio.stopListening();
}

String jsonParser(String name, String value) {
  return "{\"" + name + "\":" + value + "}";
}







