package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;

import android.util.Log;
import android.widget.Button;


import java.util.ArrayList;


public class EmptySeatsView extends AppCompatActivity {



    private TextToSpeech textToSpeech;


    // Stühle eingabe von mqtt? hier?
    int[] seat = {1,1,0,1,0,1};
    ArrayList<Integer> seatsList;

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

        seatsList = new ArrayList<Integer>();

        seatsList.add(R.id.seat1);
        seatsList.add(R.id.seat2);
        seatsList.add(R.id.seat3);
        seatsList.add(R.id.seat4);
        seatsList.add(R.id.seat5);
        seatsList.add(R.id.seat6);


        for (int i = 0; i < seat.length; i++) {

            int stuhlNummer = i;


            String seatString = "R.id.seat" + i;
            System.out.println(seatString);
            // int intSeat = Integer.parseInt(seatString);
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

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Verbinde dich mit dem MqttService
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Löse die Verbindung zum Service auf
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

}
