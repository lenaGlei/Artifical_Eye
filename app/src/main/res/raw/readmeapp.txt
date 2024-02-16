The application features two distinct modes tailored for different user needs:
Developer Mode and Blind User Mode.

The app controls a Raspberry Pi, which uses object recognition via a camera to determine the availability of six chairs. Communication between the hardware and the app takes place via mqtt and several topics.


Blind User Mode

Initial Interaction:
Waiting for Raspberry Pi Readiness: Right after the app is opened, users are greeted with an audio message instructing them to wait until the Raspberry Pi is ready. This readiness is signaled by a piReady message, indicating that a stable MQTT connection has been established on the app and the Raspberry Pi.

Start Object Detection: Once the Raspberry Pi is ready, users are prompted with a sound to tap the center of the screen. This action sends a start command to the Raspberry Pi, triggering the object detection process to identify the availability of seats.

Object Detection and Results:
Receiving Seat Availability Data:
Once the Raspberry Pi finishes detecting seats and sends out the occupancy data, the app directly moves to the empty seats view. This screen shows which seats are taken or free, and and a audio reads the results out.

Immediate Results on Demand:
Users pressing the screen's center again request immediate seat availability from the Raspberry Pi with a getResults command. This quick method may not always be as accurate, depending on whether the detection process is complete. For the most reliable information, it's better to wait for the automatic update.

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
