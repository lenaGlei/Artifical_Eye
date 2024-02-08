package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.media.AudioAttributes;
import android.media.AudioManager;

import java.util.Locale;



public class Cybathlon extends AppCompatActivity {
    private MediaPlayer player;

    private boolean isBound = false;
    private MqttService mqttService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MqttService.LocalBinder binder = (MqttService.LocalBinder) service;
            mqttService = binder.getService();
            isBound = true;
            // Du kannst jetzt Methoden auf mqttService aufrufen
            mqttService.publish(mqttService.getPublishTopic(), "start");
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cybathlon);

        // Verbinde dich mit dem MqttService
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);



        Button button = findViewById(R.id.btn_CybathlonActive);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                vibrateNow(500);


                Intent intent = new Intent(Cybathlon.this, EmptySeatsView.class);
                startActivity(intent);
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
        player = MediaPlayer.create(Cybathlon.this, R.raw.sound2);
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
    public void onBackPressed() {
        // Stop TextToSpeech if it's speaking


        // Stop MediaPlayer if it's playing
        if (player != null && player.isPlaying()) {
            player.stop();
        }

        // Call super method for default back behavior
        super.onBackPressed();
    }


    protected void onStop() {
        super.onStop();




    }




}
