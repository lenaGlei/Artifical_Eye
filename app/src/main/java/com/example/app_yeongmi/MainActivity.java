package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hivemq.client.mqtt.MqttClient;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Mqtt
        //client = new SimpleMqttClient("broker.hivemq.com", 1883, UUID.randomUUID().toString());





        // Sound
        MediaPlayer player= MediaPlayer.create(MainActivity.this,R.raw.sound1);
        player.start();







        Button button = findViewById(R.id.btn_start);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    player.release();





                Intent intent= new Intent(MainActivity.this, BlindUser2.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.btn_developer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(MainActivity.this, DeveloperUser2.class);
                startActivity(intent);
            }
        });







    }
}
