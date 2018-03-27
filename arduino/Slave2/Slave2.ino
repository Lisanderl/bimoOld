#include <Arduino.h>
#include <Motor.h>
#include <BimoControl.h>
#include <BimoSettings.h>
#include <nRF24L01.h>
#include <printf.h>
#include <RF24.h>
#include <RF24_config.h>
#include <SPI.h>
#include <Ultrasonic.h>

//NRF24
RF24 radio(A0, A1);
//Voltage
int voltagePin = 2;
//ECHO
int trigPin = A3;
int echoPin = A2;
Ultrasonic ultrasonic(trigPin, echoPin);
//motors control
Motor m1(10, 4, 3);
Motor m2(9, 7, 8);
BimoSettings settings;
//led/tone control
int blincPin = 2;// for tests
int led1Pin = 5;
int tonePin = A5;
BimoControl bimo(m1, m2, ultrasonic, settings, led1Pin, tonePin);

bool connection;
void configs() {

  TCCR1B = TCCR1B & 0b11111000 | 5; //set PWM to low frecency
  analogReference(INTERNAL);
  pinMode(blincPin, OUTPUT);
  pinMode(led1Pin, OUTPUT);
  pinMode(tonePin, OUTPUT);
  tone(tonePin, random(10, 800), 100);
  delay(30);
  nrfSetup();
  tone(tonePin, random(10, 800), 100);
  delay(30);
  blinc(10, 2);
  bimo.ledON();
  blinc(30, 2);
  tone(tonePin, random(10, 800), 100);
  delay(30);
  tone(tonePin, random(10, 800), 100);
  delay(30);
  tone(tonePin, random(10, 800), 100);
  bimo.ledON();
  delay(30);
  bimo.ledON();
}

void nrfSetup() {
  bool master = false;

  byte addr1[] = {0xC2, 0xC2, 0xC2, 0xC2, 0xC2};
  byte addr2[] = {0xE7, 0xE7, 0xE7, 0xE7, 0xE7};

  radio.begin();
  radio.txDelay = 135;
  radio.enableDynamicPayloads();
  radio.setCRCLength( RF24_CRC_16 ) ;
  radio.setChannel(4);  // Channel number - 0-125
  radio.setDataRate(RF24_1MBPS);
  radio.setPALevel(3);
  radio.setRetries(1, 3); // 1 - delay, 2 - amount
  radio.setAutoAck( true ) ;

  radio.openWritingPipe(addr1);
  radio.openReadingPipe(1, addr2);

  radio.powerUp();
  blinc(100, 10);
  radio.startListening();
  blinc(10, 2);
}

void setup() {
  configs();
  nrfSetup();
}

void loop() {
  radioListener();
  echoListener(settings.echoActive);
  voltageListener(settings.sendVoltage, settings.voltageCount);
  connectionListener(settings.connectionCount);
}

// comunicatins with radio
void parseDataFromRadio() {
  char requestData[32];
  if (radio.available()) {
    int len = radio.getDynamicPayloadSize();
    radio.read(requestData, len);
    bimo.findAndGetDataFromArray(requestData);
  }
}

void sendJson(String json) {
  radio.stopListening();
  delay(1);

  int len = json.length() + 1;
  char strArray(len);
  json.toCharArray(strArray, len);
  radio.write(strArray, len);

  delay(1);
  radio.startListening();
}

String jsonParser(String name, String value) {
  return "{\"" + name + "\":" + value + "}";
}

void blinc(int ms,  int n ){
for(int i = n; i != 0; i--){
  digitalWrite(blincPin, HIGH);
  delay(ms/n);
  digitalWrite(blincPin, LOW);
  delay(ms/n);
 }
}
// listeners
void echoListener(bool activeFlag){
  if(activeFlag){
    if(bimo.measureEchoValue() <= settings.echoDistance){
      bimo.stopMove();
      bimo.alarm();
    }
  }
}

void voltageListener(bool activeFlag, long count){
  count++;
 if(activeFlag&&(count > 5000000)){
  String json = jsonParser("V", String(analogRead(voltagePin), DEC));
  sendJson(json);
  settings.voltageCount = 0;
 }
}

void radioListener(){
  if (radio.available()) {
    blinc(6, 1);
    parseDataFromRadio();
  }
}

void connectionListener(long count) {
  count++;
  if (count == 10000000) {
    String json = jsonParser("C", "1");
    sendJson(json);
    settings.isConnected = false;
  }
  if ((count == 13000000)&&(!settings.isConnected)) {
     count=0;
     if(bimo.isMoving()){
     bimo.stopMove();
     }
  }
}

