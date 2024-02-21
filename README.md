# App Documentation

## Overview
The application features two distinct modes tailored for different user needs: Developer Mode and Blind User Mode.

The app controls a Raspberry Pi, which uses object recognition via a camera to determine the availability of six chairs. Communication between the hardware and the app takes place via mqtt and several topics.

## Modes
### Blind User Mode

#### Initial Interaction:
Waiting for Raspberry Pi Readiness: Right after the app is opened, users are greeted with an audio message instructing them to wait until the Raspberry Pi is ready. This readiness is signaled by a "piReady" message (topic: emptySeats/HardwareToApp), indicating that a stable MQTT connection has been established on the app and the Raspberry Pi.

#### Start Object Detection: 
Once the Raspberry Pi is ready, users are prompted with a sound to tap the center of the screen. This action sends a "start" command to the Raspberry Pi (topic: emptySeats/AppToHardware), triggering the object detection process to identify the availability of seats.

#### Object Detection and Results:
##### Navigation: 
If the user is very close to an object, indicating a higher need for caution, the sensor sends a signal of "1" (topic: emptySeats/Navigation). The app responds by emitting a double beep sound. For objects that are near but not in immediate proximity, the sensor sends a signal of 2. In response, the app generates a single beep sound.

##### Receiving Seat Availability Data:
Once the Raspberry Pi finishes detecting seats and sends out the occupancy data, the app directly moves to the empty seats view. This screen shows which seats are taken or free, and and a audio reads the results out ("[0,1,0,1,0,0]" topic: "emptySeats/HardwareToApp).

##### Immediate Results on Demand:
Users pressing the screen's center again request immediate seat availability from the Raspberry Pi with a "getResults" command (topic: "emptySeats/AppToHardware"). This quick method may not always be as accurate, depending on whether the detection process is complete. For the most reliable information, it's better to wait for the automatic update.

#### Audio feedback and Text to speech
In each activity a audio feedback tells the user what is intened to be done next or the restults from the seat detection. The audio commands during the use are fixed .wav files. The output for the seat avilibilty is done by textToSpeech in the choosen language, by default in english.

#### Audio Recognition for Command Repeat:
After the seat availability results have been announced, the app initiates an audio recognition phase. During this phase, users can vocally command the app to "repeat" the results.

#### Vibration feedback:
When pressing a button that the blind person is intended to press, there is a haptic feedback by a 0.5 second long vibrition.


### Developer Mode:

##### Access: 
Entry requires a double-click on a specific developer button, with an additional confirmation screen to prevent accidental access by blind users.
Subsequently, a sign-up and login process allows access to the settings.

#### Features:
##### MQTT Connection:
Displays detailed connection information. Users can edit and apply topic changes through respective buttons, enhancing customization for testing and publishing.
A QR code can be generated to open to the HiveMQ page for testing and publishing.

##### Detection Dashboard:
Presents a picture from the seat detection process along with the visualisation of the results and critical logs.

##### Language Setting:
For the empty seats view, users can toggle between English and German, catering to broader user preferences.

##### Cybathlon Website:
Direct access to the Cybathlon website through the app, providing users with additional information.

##### Instruction:
A scrollable text view displays the app's instructions.

##### About Us:
Offers personal insights into the team behind the app and networking opportunities via the Likedin profiles.
