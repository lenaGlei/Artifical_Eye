package com.example.app_yeongmi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.hivemq.client.internal.mqtt.util.MqttChecks.publish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;

import android.os.IBinder;

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

    private final String publishTopic = "emptySeats/AppToHardware";
    private SimpleMqttClient client;
    private TextView txtTemp;
    int i = 0;
    private MediaPlayer player;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Starten Sie den MQTT-Service
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        startService(mqttServiceIntent);
        //Intent publishIntent = new Intent(this, MqttService.class);





        Button button1 = findViewById(R.id.btn_start);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



               // publishIntent.setAction(MqttService.ACTION_PUBLISH);
                //publishIntent.putExtra(MqttService.EXTRA_TOPIC, "emptySeats/AppToHardware");
                //publishIntent.putExtra(MqttService.EXTRA_MESSAGE, "start");
                //startService(publishIntent);



                vibrateNow(500);

                Intent intent= new Intent(MainActivity.this, Cybathlon.class);
                startActivity(intent);




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

        // Start playing the sound when the activity starts
        player = MediaPlayer.create(MainActivity.this, R.raw.sound1);
        player.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Release the MediaPlayer when the activity is paused
        if (player != null) {
            player.release();
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
    }



}
