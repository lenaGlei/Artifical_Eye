package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;

import android.util.Log;
import android.widget.Button;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class EmptySeatsView extends AppCompatActivity {

    private TextToSpeech textToSpeech;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Verbinde dich mit dem MqttService
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(mqttMessageReceiver,
                new IntentFilter("com.example.app.MQTT_MESSAGE"));

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
                Log.d("MQTT", "Nachricht erhalten in Activity: " + payload);

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
    }

}
