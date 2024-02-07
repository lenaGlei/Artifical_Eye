package com.example.app_yeongmi;

import static android.Manifest.permission.RECORD_AUDIO;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;


public class EmptySeatsView extends AppCompatActivity {



    private TextToSpeech textToSpeech;


    // Stühle eingabe von mqtt? hier?
    //int[] seat = {1,1,0,1,0,1};
    ArrayList<Integer> seatsList;
    private SpeechRecognizer speechRecognizer;
    private static final int RECORD_AUDIO_REQUEST_CODE = 1;

    private boolean isBound = false;
    private MqttService mqttService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MqttService.LocalBinder binder = (MqttService.LocalBinder) service;
            mqttService = binder.getService();
            isBound = true;
            // Du kannst jetzt Methoden auf mqttService aufrufen
            mqttService.publish("emptySeats/AppToHardware", "getResults");
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_seats_view);



        // Speech recognition
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new MyRecognitionListener());

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);


        } else {

            initializeSpeechRecognizerAfterDelay();
        }

        //
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.UK);
                    Log.d("TextToSpeech", "Text-to-Speech-Initialisierung erfolgreich");
                    //pruefeSitzStatus(seat);

                }
            }
        });





        // Seat colour

        seatsList = new ArrayList<Integer>();

        seatsList.add(R.id.seat1);
        seatsList.add(R.id.seat2);
        seatsList.add(R.id.seat3);
        seatsList.add(R.id.seat4);
        seatsList.add(R.id.seat5);
        seatsList.add(R.id.seat6);

        // Verbinde dich mit dem MqttService
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(mqttMessageReceiver,
                new IntentFilter("com.example.app.MQTT_MESSAGE"));


        /*for (int i = 0; i < seat.length; i++) {

            int stuhlNummer = i;


            String seatString = "R.id.seat" + i;
            System.out.println(seatString);

            if (seat[i] == 1) {
                Button upperscreen = findViewById(seatsList.get(i));
                upperscreen.setBackgroundColor(Color.RED);



                Log.d("StuhlActivity", "Stuhl " + stuhlNummer + " ist belegt.");
            } else {
                Button upperscreen = findViewById(seatsList.get(i));
                upperscreen.setBackgroundColor(Color.GREEN);


                Log.d("StuhlActivity", "Stuhl " + stuhlNummer + " ist leer.");
            }
        }
*/


    }

        //Function for text to speech of seats

    private void pruefeSitzStatus(int[] seat) {
        StringBuilder ausgabe = new StringBuilder();

        for (int i = 0; i < seat.length; i++) {

            Log.d("Empty SeatsView", String.format("i = %d", i));
            if (seat[i] == 1) {


                ausgabe.append("Seat ").append(i + 1).append(" is occupied. ");
            } else {
                ausgabe.append("Seat ").append(i + 1).append(" is free. ");
            }
        }

        sprecheText(ausgabe.toString());
    }


    private void sprecheText(String text) {
        if (textToSpeech != null) {

            String newText = text + "Say repeat if you want to hear the sound again";
            textToSpeech.speak(newText, TextToSpeech.QUEUE_ADD, null, null);
        }
    }



        // Class for recognition listener

    private class MyRecognitionListener implements RecognitionListener {


        @Override
        public void onReadyForSpeech(Bundle params) {
            // Called when the speech recognition service is ready to receive speech input.
        }

        @Override
        public void onBeginningOfSpeech() {
            // Called when the user starts to speak.
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // Called when the RMS (root mean square) of the incoming audio changes.
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            // Called when the user stops speaking.
        }

        @Override
        public void onError(int error) {
            // Called when there's an error in the speech recognition process.
            Log.e("SpeechRecognition", "Error code: " + error);


            Toast.makeText(EmptySeatsView.this, "Error in speech recognition", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // Called when speech recognition results are available.
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (matches != null && !matches.isEmpty()) {
                String command = matches.get(0).toLowerCase();
                if (command.contains("Repeat")) {
                    // Handle the "hello" command, e.g., start a new activity or perform an action

                    Toast.makeText(EmptySeatsView.this, "Repeat command recognized!", Toast.LENGTH_SHORT).show();
                    //pruefeSitzStatus(seatStatus);
                }
            }

            // Restart voice recognition
            initializeSpeechRecognizerAfterDelay();

        }

        private void pruefeSitzStatus(int[] seat) {
            StringBuilder ausgabe = new StringBuilder();

            for (int i = 0; i < seat.length; i++) {

                Log.d("Empty SeatsView", String.format("i = %d", i));
                if (seat[i] == 1) {


                    ausgabe.append("Seat ").append(i + 1).append(" is occupied. ");
                } else {
                    ausgabe.append("Seat ").append(i + 1).append(" is free. ");
                }
            }

            sprecheText(ausgabe.toString());
        }


        private void sprecheText(String text) {
            if (textToSpeech != null) {

                String newText = text + "Say repeat if you want to hear the sound again";
                textToSpeech.speak(newText, TextToSpeech.QUEUE_ADD, null, null);

            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // Called when partial recognition results are available.
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // Called when events related to the speech recognition process occur.
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Release the SpeechRecognizer when the activity is destroyed
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }

    // Speech recognizer
    private void initializeSpeechRecognizer() {

        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        }
        speechRecognizer.startListening(recognizerIntent);
    }

    // make delay for speech recognizer

    private void initializeSpeechRecognizerAfterDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeSpeechRecognizer();
            }
        }, 15000); // 15 seconds delay
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mqttMessageReceiver);
        // Löse die Verbindung zum Service auf
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        super.onStop();
    }

    private BroadcastReceiver mqttMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.example.app.MQTT_MESSAGE".equals(intent.getAction())) {
                String payload = intent.getStringExtra("payload");
                Log.d("MQTT", "Nachricht erhalten in EmptySeatsActivity: " + payload);
                // Konvertiere die Payload in ein Array von Integern
                try {
                    JSONArray jsonArray = new JSONArray(payload);
                    int[] seatStatus = new int[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        seatStatus[i] = jsonArray.getInt(i);
                        Log.d("MQTT", "seatStatus: " + seatStatus[i]);
                    }
                    // UI-Update auf dem Main-Thread
                    EmptySeatsView.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateSeats(seatStatus);
                        }
                    });
                } catch (JSONException e) {
                    Log.e("MQTT", "Fehler beim Parsen der Payload", e);
                }
            }
        }
    };
    private void updateSeats(int[] seatStatus) {
        for (int i = 0; i < seatStatus.length; i++) {
            Button seatButton = findViewById(seatsList.get(i));
            if (seatStatus[i] == 1) {
                // Sitz ist belegt
                seatButton.setBackgroundColor(Color.RED);
            } else {
                // Sitz ist frei
                seatButton.setBackgroundColor(Color.GREEN);
            }
        }
        //hier audio ausgabe ??
        pruefeSitzStatus(seatStatus);
    }





}
