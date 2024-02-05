There are two implemented modes within the application: Developer Mode and Blind User Mode. The Raspberry Pi provides information about available seats through an MQTT connection.

Upon entering the app, a sound prompts the user to press in the middle of the screen to enter Blind User Mode. Another sound instructs the user to press in the middle again to obtain information about seat availability when reaching the table. This leads the user to the final screen where green and red buttons, along with an audio cue, indicate whether the seat is taken or free. A confirmation vibration follows each button press.

To access Developer Mode, a double click on the developer button is required, followed by a screen to ensure the user didn't press the button accidentally. Subsequently, a sign-up and login process allows access to MQTT connection data. A QR code can be generated to open to the HiveMQ page for testing and publishing.

