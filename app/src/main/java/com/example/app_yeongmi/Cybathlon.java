package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.media.AudioAttributes;
import android.media.AudioManager;


public class Cybathlon extends AppCompatActivity {



    //MediaPlayer mediaPlayer1;
    //MediaPlayer mediaPlayer2;


    // muss kontinuierlich kommen???
    //int[] distance = {1, 1, 0, 1, 0, 1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cybathlon);

        Button button = findViewById(R.id.btn_CybathlonActive);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Cybathlon.this, EmptySeatsView.class);
                startActivity(intent);
            }
        });

        /*
        // ab hier sound
        mediaPlayer1 = MediaPlayer.create(this, R.raw.beepsound);
        mediaPlayer2= MediaPlayer.create(this,R.raw.beepsound2);

        sound();

         */





    }

    /*private void sound(){

    for (int i = 0; i < distance.length; i++) {

        Log.d("Cybathlon", String.format("i = %d", i));


        if (distance[i] == 0) {

            // mediaPlayer1.start();
            playSound(mediaPlayer1);

        } else {
            //mediaPlayer2.start();
            playSound(mediaPlayer2);


        }
    }
    }

    private void playSound(final MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Release the MediaPlayer after sound is played
                mp.release();
            }
        });
    }

     */






}
