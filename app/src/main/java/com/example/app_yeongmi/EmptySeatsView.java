package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;
import java.util.UUID;

public class EmptySeatsView extends AppCompatActivity {

    //private Button btn;
   // private static final String TAG = "EmptySeatsView";
   // private String topic, clientID;
    //private MqttAndroidClient client;
    private TextToSpeech textToSpeech;


    // Stühle eingabe von mqtt? hier?
    int[] seat = {1,1,0,1,0,1};



    private BroadcastReceiver mqttMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Überprüfe, ob die empfangene Action die ist, die wir erwarten
            if ("com.example.app_yeongmi.MQTT_MESSAGE".equals(intent.getAction())) {
                // Extrahiere die Daten aus dem Intent
                String topic = intent.getStringExtra("topic");
                String message = intent.getStringExtra("message");

                // Hier kannst du nun die Nachricht verarbeiten und z.B. auf der UI anzeigen
                //TextView textView = findViewById(R.id.yourTextViewId); // Ersetze mit deiner TextView ID
                //textView.setText(String.format("Topic: %s, Message: %s", topic, message));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_seats_view);

        // Registrieren des BroadcastReceivers, um auf MQTT-Nachrichten zu hören
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mqttMessageReceiver,
                new IntentFilter("com.example.app_yeongmi.MQTT_MESSAGE")
        );
    }

    @Override
    protected void onDestroy() {
        // Deregistrieren des BroadcastReceivers, um Speicherlecks zu vermeiden
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mqttMessageReceiver);
        super.onDestroy();
    }


    }
