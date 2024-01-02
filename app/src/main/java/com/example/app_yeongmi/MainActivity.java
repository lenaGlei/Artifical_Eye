package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int sound1




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaPlayer mMediaPlayer = new MediaPlayer();
        //mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.sound1);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams()
                    .setAudioAttributes()
                    .build();
        }else {
            soundPool=new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        sound1= soundPool.load(this, R.raw.sound1, 1);


    }

    public void playSound(View v) {
        soundPool.play(sound1, 1, 1, 0,0,1);

    } */

}