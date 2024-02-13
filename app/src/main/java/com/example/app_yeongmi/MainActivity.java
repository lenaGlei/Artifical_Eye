package com.example.app_yeongmi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.hivemq.client.internal.mqtt.util.MqttChecks.publish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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


public class MainActivity extends AppCompatActivity {

    private final String publishTopic = "emptySeats/AppToHardware";
    private SimpleMqttClient client;
    private TextView txtTemp;
    int i = 0;
    private MediaPlayer player;
    private MediaPlayer player2;

    private boolean isFirstTime = true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        player2 = MediaPlayer.create(MainActivity.this, R.raw.sound4);
        player2.start();


        // Starten Sie den MQTT-Service
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        startService(mqttServiceIntent);


        LocalBroadcastManager.getInstance(this).registerReceiver(mqttMessageReceiver,
                new IntentFilter("com.example.app.MQTT_MESSAGE"));





        Button button1 = findViewById(R.id.btn_start);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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




    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isFirstTime){


                player = MediaPlayer.create(MainActivity.this, R.raw.sound1);
                player.start();

            }

    isFirstTime = false;

    }





    @Override
    protected void onPause() {
        super.onPause();


        if (player != null) {
            player.release();
        }
        if (player2 != null) {
            player2.release();
        }


    }


    @Override
    public void onBackPressed() {



        if (player != null && player.isPlaying()) {
            player.stop();
        }
        if (player2 != null && player2.isPlaying()) {
            player2.stop();
        }


        // Call super method for default back behavior
        super.onBackPressed();
    }

    protected void onStop() {
        super.onStop();


    }




    // wenn PiReady empfangen wurde wird der Startbutton freigegeben um die emptySeatsdetection zustarten
    private BroadcastReceiver mqttMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.example.app.MQTT_MESSAGE".equals(intent.getAction())) {
                String payload = intent.getStringExtra("payload");
                MqttLogger.log("MQTT","piReady im logger");
                if ("piReady".equals(payload)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Button btnStart = findViewById(R.id.btn_start);
                            btnStart.setEnabled(true);
                            // audio hier
                            player = MediaPlayer.create(MainActivity.this, R.raw.sound1);
                            player.start();
                        }
                    });
                }
            }
        }
    };



}
