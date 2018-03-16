#include <ArduinoJson.h>
#include <BimoControl.h>
#include <nRF24L01.h>
#include <printf.h>
#include <RF24.h>
#include <RF24_config.h>
#include <SPI.h>
#include <Ultrasonic.h>

#define CONTROLS (d=='W'||d=='S'||d=='A'||d=='D'||d=='C'||d=='L')

//motors control
Motor m1(10, 4, 3);
Motor m2(9, 7, 8);
BimoControl bimo(m1, m2);
//NRF24
RF24 radio(A0, A1);
char data[32];
int d ;
//ECHO
int trigPin = A3;
int echoPin = A2;
Ultrasonic ultrasonic(trigPin, echoPin);
int distance = 5;
//led/tone control
int ledPin = 2;// for tests
int led1Pin = 5;
int led2Pin = 6;
int tonePin = A5;
unsigned int led1Mode = 0;
DynamicJsonBuffer jsonBuffer(128);

void setup() {
  configs();
}

void loop() {
  while(true){
  distanceReaction();

  if (radio.available()) {
    bimo.blinc(5);
    char len = radio.getDynamicPayloadSize();
    radio.read(data, len);
    d = data[0];
    if (CONTROLS) {
      bimo.blinc(5);
      interpretator();
    } else if (d == 'G') {
      char str[4];
      int i = analogRead(7);
      bimo.blinc(5);
      //sendADC();
      itoa(i, str, 10);
      dataSendWrapper('d', str, 4);
    }
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
}
void parseDataFromRadio() {
  char requestData[32];
  if (radio.available()) {
    int len = radio.getDynamicPayloadSize();
    radio.read(requestData, len);
    JsonObject& root = jsonBuffer.parseObject(requestData);
  }
}
//TO DO: creat default values of adjustments like pwm and adc...
void sendADC() {
  int sizeADC;
  int i;
  char str[4];
  radio.stopListening();
  i = analogRead(7);
  itoa(i, str, 10);
  if (i < 1000) {
    sizeADC = 3;
  } else {
    sizeADC = 4;
  }
  radio.write(str, sizeADC);
  i = analogRead(6);
  itoa(i, str, 10);
  if (i < 1000) {
    sizeADC = 3;
  } else {
    sizeADC = 4;
  }
  radio.write(str, sizeADC);
  radio.startListening();
  delay(1);
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
      } bimo.ledON(led1Pin, ++led1Mode);
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

void distanceReaction() {
  distance = (int) round(ultrasonic.Ranging(CM));

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
  bimo.ledON(led1Pin, 1);
  sendADC();
  bimo.blinc(30);
  tone(tonePin, random(10, 800), 100);
  delay(30);
  tone(tonePin, random(10, 800), 100);
  delay(30);
  tone(tonePin, random(10, 800), 100);
  bimo.ledON(led1Pin, 5);
  delay(30);
  bimo.ledON(led1Pin, 0);
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
