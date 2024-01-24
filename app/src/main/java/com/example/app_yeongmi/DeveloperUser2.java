package com.example.app_yeongmi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DeveloperUser2 extends AppCompatActivity {

    MediaPlayer mMediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_user2);


        mMediaPlayer = MediaPlayer.create(this, R.raw.sound3);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.start();


        Button button1 = findViewById(R.id.btn_goback);
        Button button2 = findViewById(R.id.btn_continueDevelop);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DeveloperUser2", "Button zurück clicked");
                Intent intent= new Intent(DeveloperUser2.this, MainActivity.class);
                startActivity(intent);
            }
        });



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DeveloperUser2", "Button continueDevelop clicked");
                Intent intent= new Intent(DeveloperUser2.this, DevLoginActivity.class);
                startActivity(intent);
            }
        });

    }
}