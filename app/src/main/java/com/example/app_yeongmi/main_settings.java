package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class main_settings extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        ImageButton cybathlonWeb_btn = findViewById(R.id.CybathlonWeb_btn);

        String email = getIntent().getStringExtra("EMAIL");

        // Find the TextView in the layout
        TextView textViewEmail = findViewById(R.id.username);

        // Set the email in the TextView
        textViewEmail.setText("Welcome " + email);



        ImageView imageViewBack = findViewById(R.id.btn_backSetting);


        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_settings.this, MainActivity.class);
                startActivity(intent);
            }
        });


        cybathlonWeb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://cybathlon.ethz.ch/de/event/disziplinen/vis";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });


        ImageButton mqttConnection_btn = findViewById(R.id.mqtt_btn);
        mqttConnection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(main_settings.this, DeveloperData.class);
                startActivity(intent);
            }
        });

        ImageButton instruction_btn = findViewById(R.id.instruction_btn);
        instruction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(main_settings.this, Instructions.class);
                startActivity(intent);
            }
        });

    }




}