package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetectionDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_dashboard);

        TextView textViewLogs = findViewById(R.id.textViewLogs);
        textViewLogs.setText(MqttLogger.getLogs());

        ImageView imageViewBack = findViewById(R.id.btn_backSetting);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectionDashboard.this, main_settings.class);
                startActivity(intent);
            }
        });

    }


}