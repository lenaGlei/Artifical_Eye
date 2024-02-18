package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class main_settings extends AppCompatActivity {

    public String  email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        setWelcomeName();

        //  Back button to navigate to the Start screen
        ImageView imageViewBack = findViewById(R.id.btn_backSetting);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_settings.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Cybathlon Webside of if button is clicked
        ImageButton cybathlonWeb_btn = findViewById(R.id.CybathlonWeb_btn);
        cybathlonWeb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://cybathlon.ethz.ch/de/event/disziplinen/vis";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });


        // following buttons are linked to open the acitivitys
        ImageButton mqttConnection_btn = findViewById(R.id.mqtt_btn);
        mqttConnection_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(main_settings.this, DeveloperData.class);
                startActivity(intent);
            }
        });

        ImageButton dashboard_btn = findViewById(R.id.dashboard_btn);
        dashboard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(main_settings.this, DetectionDashboard.class);
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


        ImageButton language_btn = findViewById(R.id.language_btn);
        language_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_settings.this, languageSwitch.class);
                startActivity(intent);
            }
        });

        ImageButton aboutUs_btn = findViewById(R.id.AboutUs_btn);
        aboutUs_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_settings.this, AboutUs.class);
                startActivity(intent);
            }
        });
    }

    void setWelcomeName() {
        email = getIntent().getStringExtra("EMAIL");
        TextView textViewEmail = findViewById(R.id.username);

        // check if email is available
        if (email != null && !email.isEmpty()) {
            textViewEmail.setText("Welcome " + email);
        } else {
            // if not show only "Welcome"
            textViewEmail.setText("Welcome");
        }
    }
}