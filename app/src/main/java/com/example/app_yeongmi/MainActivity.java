package com.example.app_yeongmi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import com.example.app_yeongmi.mqtt.SimpleMqttClient;
import com.example.app_yeongmi.mqtt.data.MqttMessage;
import com.hivemq.client.internal.mqtt.message.publish.MqttPublish;


public class MainActivity extends AppCompatActivity {

    private static final String topic = "emptySeats/testtopic1";
    private SimpleMqttClient client;
    private TextView txtTemp;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Starten Sie den MQTT-Service
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        startService(mqttServiceIntent);
        //SimpleMqttClient mqttClient = MqttService.getMqttClient();


        // Sound
        MediaPlayer player= MediaPlayer.create(MainActivity.this,R.raw.sound1);
        player.start();


        Button button1 = findViewById(R.id.btn_start);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.release();
                vibrateNow(500);

                Intent intent= new Intent(MainActivity.this, Cybathlon.class);
                startActivity(intent);

                String topic = "emptySeats/testtopic1"; // Das gewünschte Topic
                String message = "Hallo Jonna, wie geht es dir?"; // Die zu sendende Nachricht

                //publishMessage(topic, message);
            }
        });

        Button button2 = findViewById(R.id.btn_developer);


        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                i++;
                Handler handler = new Handler();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        i = 0;
                    }
                };

                if (i == 1) {
                    //Single click
                    handler.postDelayed(r, 250);
                } else if (i == 2) {
                    //Double click
                    i = 0;
                    Intent intent = new Intent(MainActivity.this, DeveloperUser2.class);
                    startActivity(intent);

                }


            }
        });






    }

    private void vibrateNow (long millis){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE))
                    .vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(millis);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }



}
