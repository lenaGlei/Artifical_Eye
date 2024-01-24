package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DeveloperUser2 extends AppCompatActivity {

    MediaPlayer mMediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_user2);


        mMediaPlayer = MediaPlayer.create(this, R.raw.sound3);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.start();



        Button button1 = findViewById(R.id.btn_ObjectAvoidance);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(BlindUser2.this, ObjectAvoidance.class);
                startActivity(intent);
            }
        });

    }
}