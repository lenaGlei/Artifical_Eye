package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetectionDashboard extends AppCompatActivity {

    private int[] seatStatus;
    private MqttService mqttService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MqttService.LocalBinder binder = (MqttService.LocalBinder) service;
            mqttService = binder.getService();
            isBound = true;

            seatStatus = mqttService.getSeatStatus();
            if (seatStatus != null) {
                updateSeatColors(seatStatus);
            } else {
                Log.d("MQTT","kein seatstatus da im dashboard");
            }

        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

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

        // Verbinde dich mit dem MqttService
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        MqttLogger.log("MQTT","Dashboard start");
        if(MqttService.getLastScreenshot()!= null) {
            Log.d("MQTT","screeshot ist null");
            Bitmap screenshot = MqttService.getLastScreenshot();
            updateScreenshot(screenshot);
            MqttLogger.log("MQTT","Got screenshot");
        }
        else {
            Bitmap screenshot = BitmapFactory.decodeResource(getResources(), R.drawable.linkedinbitmap);
            updateScreenshot(screenshot);
            Log.d("MQTT","screeshot ist nicht da am board");
            MqttLogger.log("MQTT","Got no screenshot");
        }


        //updateSeatColors(seatStatus);
    }

    public void updateScreenshot(Bitmap screenshot) {
        Log.d("MQTT","screeshot wird verarbeitet");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageView screenshotView = findViewById(R.id.Screenshot);
                screenshotView.setImageBitmap(screenshot);
            }
        });
    }

    public void updateSeatColors(int[] seats) {
        View[] seatViews = new View[]{
                findViewById(R.id.seat1),
                findViewById(R.id.seat2),
                findViewById(R.id.seat3),
                findViewById(R.id.seat4),
                findViewById(R.id.seat5),
                findViewById(R.id.seat6)
        };

        for (int i = 0; i < seatViews.length; i++) {
            if (seats[i] == 1) {
                // Sitz ist belegt, setze auf Rot
                seatViews[i].setBackgroundColor(getResources().getColor(R.color.rot));
            } else {
                // Sitz ist frei, setze auf Grün
                seatViews[i].setBackgroundColor(getResources().getColor(R.color.grün));
            }
        }
    }

    @Override
    protected void onStop() {
        // Löse die Verbindung zum Service auf
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        super.onStop();
    }




}