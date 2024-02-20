package com.example.app_yeongmi;


import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class MainActivity extends AppCompatActivity {

    int i = 0;
    private MediaPlayer player;
    private MediaPlayer player2;

    private boolean isFirstTime = true;
    private boolean isBound = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        player2 = MediaPlayer.create(MainActivity.this, R.raw.sound4);
        player2.start();


        // Start the Mqtt Service
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        startService(mqttServiceIntent);

        // Localbroadcast for mqtt messages
        LocalBroadcastManager.getInstance(this).registerReceiver(mqttMessageReceiver,
                new IntentFilter("com.example.app.MQTT_MESSAGE"));




        //start button -> start objectdetection
        Button btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vibrateNow(500);

                Intent intent= new Intent(MainActivity.this, Cybathlon.class);
                startActivity(intent);
            }
        });

        // developer mode button -> enter with double click
        Button btn_developer = findViewById(R.id.btn_developer);
        btn_developer.setOnClickListener(new View.OnClickListener() {
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
        // Deregister Broadcast receiver when stopping the activity
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mqttMessageReceiver);
        super.onStop();
    }




    // when signal from pi is received , start button gets enabled to start emptySeats detection
    private BroadcastReceiver mqttMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        if ("com.example.app.MQTT_MESSAGE".equals(intent.getAction())) {
            String payload = intent.getStringExtra("payload");
            if ("piReady".equals(payload)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button btnStart = findViewById(R.id.btn_start);
                        btnStart.setEnabled(true);
                        // audio
                        player = MediaPlayer.create(MainActivity.this, R.raw.sound1);
                        player.start();
                    }
                });
            }
        }
        }
    };

}
