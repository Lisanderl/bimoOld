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
char data[32];
int d ;
//ECHO
int trigPin = A3;
int echoPin = A2;
Ultrasonic ultrasonic(trigPin, echoPin);
int distance = 5;
//motors control
Motor m1(10, 4, 3);
Motor m2(9, 7, 8);
BimoSettings settings;
BimoControl bimo(m1, m2, ultrasonic, settings);
//led/tone control
int ledPin = 2;// for tests
int led1Pin = 5;
int led2Pin = 6;
int tonePin = A5;
unsigned int led1Mode = 0;

long connectionFlag = 0;
bool connection;
void setup() {
  configs();
}

void loop() {
  //TO DO: add connection 

  if (radio.available()) {
    bimo.blinc(5);
    char len = radio.getDynamicPayloadSize();
    radio.read(data, len);

  }

  if (distance < 6) {
    bimo.setMotorPWM(70, 70); bimo.goBack();
    delay(random(10, 150));
    bimo.setMotorPWM(100, 100); bimo.goBack();
    delay(random(10, 150));
    bimo.setMotorPWM(130, 130); bimo.goBack();
    delay(random(10, 100));
    tone(tonePin, 700, 50);
    bimo.stopMove();
  }
}
void parseDataFromRadio() {
  char requestData[32];
  if (radio.available()) {
    int len = radio.getDynamicPayloadSize();
    radio.read(requestData, len);
    
  }
}

void connectionController() {
  connectionFlag++;
  if (connectionFlag == 10000000) {
    //send request
  }
  if (connectionFlag == 15000000) {
     connectionFlag=0;
  }

}

void interpretator() {

  switch (d) {
    case '1':  if (distance > 5) {
        bimo.setMotorPWM(150, 150);
        bimo.goStraight();
      }
      break;
    case '2':  bimo.setMotorPWM(130, 130); bimo.goBack();
      break;
    case '4':  bimo.setMotorPWM(130, 130); bimo.goLeft();
      break;
    case '7':  bimo.setMotorPWM(130, 130); bimo.goRight();
      break;
    case '5':  //set slowly right
      break;
    case '8':  //set slowly left
      break;
    case '6':  //set back slowly right
      break;
    case '9':  //set back slowly left
      break;
    case '20':   tone(tonePin, random(10, 800), 100);
      break;
    case '202': if (led1Mode == 5) {
        led1Mode = -1;
      }
      break;
    default : bimo.setMotorPWM(0, 0); bimo.stopMove();
  }
}

void dataSendWrapper(char prefix, char data[], int masSize) {
  char message[masSize + 1];
  radio.stopListening();
  message[0] = prefix;
  for (int i = 0; i < masSize; i++) {
    message[i + 1] = data[i];
  }
  radio.write(message, masSize);
  delay(1);
  radio.startListening();
}

void configs() {

  bimo.setBlincPin(ledPin);
  TCCR1B = TCCR1B & 0b11111000 | 5; //set PWM to low frecency
  analogReference(INTERNAL);
  pinMode(ledPin, OUTPUT);
  pinMode(led1Pin, OUTPUT);
  pinMode(tonePin, OUTPUT);
  tone(tonePin, random(10, 800), 100);
  delay(30);
  nrfSetup();
  tone(tonePin, random(10, 800), 100);
  delay(30);
  bimo.blinc(10);
  bimo.ledON(led1Pin);
  bimo.blinc(30);
  tone(tonePin, random(10, 800), 100);
  delay(30);
  tone(tonePin, random(10, 800), 100);
  delay(30);
  tone(tonePin, random(10, 800), 100);
  bimo.ledON(led1Pin);
  delay(30);
  bimo.ledON(led1Pin);
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
  bimo.blinc(100);
  radio.startListening();
  bimo.blinc(10);
}
