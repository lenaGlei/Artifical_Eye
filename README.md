# App Documentation

## Overview
The application features two distinct modes tailored for different user needs: Developer Mode and Blind User Mode.

The app controls a Raspberry Pi, which uses object recognition via a camera to determine the availability of six chairs. Communication between the hardware and the app takes place via mqtt and several topics.

## Modes
## Blind User Mode

### Initial Interaction:
Waiting for Raspberry Pi Readiness: Right after the app is opened, users are greeted with an audio message instructing them to wait until the Raspberry Pi is ready. This readiness is signaled by a "piReady" message (topic: emptySeats/HardwareToApp), indicating that a stable MQTT connection has been established on the app and the Raspberry Pi.

### Start Object Detection: 
Once the Raspberry Pi is ready, users are prompted with a sound to tap the center of the screen. This action sends a "start" command to the Raspberry Pi (topic: emptySeats/AppToHardware), triggering the object detection process to identify the availability of seats.

Object Detection and Results:
Navigation: 
If the user is very close to an object, indicating a higher need for caution, the sensor sends a signal of "1" (topic: emptySeats/Navigation). The app responds by emitting a double beep sound. For objects that are near but not in immediate proximity, the sensor sends a signal of 2. In response, the app generates a single beep sound.

Receiving Seat Availability Data:
Once the Raspberry Pi finishes detecting seats and sends out the occupancy data, the app directly moves to the empty seats view. This screen shows which seats are taken or free, and and a audio reads the results out ("[0,1,0,1,0,0]" topic: "emptySeats/HardwareToApp).

Immediate Results on Demand:
Users pressing the screen's center again request immediate seat availability from the Raspberry Pi with a "getResults" command (topic: "emptySeats/AppToHardware"). This quick method may not always be as accurate, depending on whether the detection process is complete. For the most reliable information, it's better to wait for the automatic update.
Audio Recognition for Command Repeat:
After the seat availability results have been announced, the app initiates an audio recognition phase. During this phase, users can vocally command the app to "repeat" the results.

Vibration feedback:
Each press of a button in blind mode is followed by a confirmation vibration.


Developer Mode:

Access: Entry requires a double-click on a specific developer button, with an additional confirmation screen to prevent accidental access by blind users.
Subsequently, a sign-up and login process allows access to the settings.

Features:
MQTT Connection:
Displays detailed connection information. Users can edit and apply topic changes through respective buttons, enhancing customization for testing and publishing.
A QR code can be generated to open to the HiveMQ page for testing and publishing.

Detection Dashboard:
Presents a picture from the seat detection process along with the visualisation of the results and critical logs.

Language Setting:
For the empty seats view, users can toggle between English and German, catering to broader user preferences.

Cybathlon Website:
Direct access to the Cybathlon website through the app, providing users with additional information.

About Us:
Offers personal insights into the team behind the app and networking opportunities via the Likedin profiles.



Upon entering the app, a sound prompts the user to press in the middle of the screen to enter Blind User Mode. Another sound instructs the user to press in the middle again to obtain information about seat availability when reaching the table. This leads the user to the final screen where green and red buttons, along with an audio cue, indicate whether the seat is taken or free. The audio can be played again by the speech command "repeat". A confirmation vibration follows each button press.

To access Developer Mode, a double click on the developer button is required, followed by a screen to ensure the user didn't press the button accidentally. Subsequently, a sign-up and login process allows access to following settings.
The following options can be selected: 
  1. the MQTT connection details can be viewed and a qr can be generated to open to the HiveMQ page for testing and publishing. In addition, the topics for subscribing and publishing messages can be changed. 
  2. The language for the speech output can be changed between english and german.
  3. A link redirects the user directly to the Cybathlon website.
  4. The Instructions to the App can be read.
  5. A short instruction "about us" can be read.
