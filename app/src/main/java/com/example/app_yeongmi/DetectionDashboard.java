package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
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

        //  Back button to navigate to the Main Settings screen
        ImageView imageViewBack = findViewById(R.id.btn_backSetting);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetectionDashboard.this, main_settings.class);
                startActivity(intent);
            }
        });

        // LocalBinder to MqttService
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkAndDisplayScreenshot();

        // Display the Logs
        TextView textViewLogs = findViewById(R.id.textViewLogs);
        textViewLogs.setText(MqttLogger.getLogs());

    }

    // Check if screenshot is available --> update Ui
    private void checkAndDisplayScreenshot() {

        if (MqttService.getLastScreenshot()!= null) {
            Log.d("MQTT", "Screenshot is available.");
            Bitmap screenshot = MqttService.getLastScreenshot();
            // Update the ImageView on the UI
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView screenshotView = findViewById(R.id.Screenshot);
                    screenshotView.setImageBitmap(screenshot);
                    MqttLogger.log("MQTT", "Screenshot is displayed on the dashboard.");
                    Log.d("MQTT","Screenshot is displayed on the dashboard.");
                }
            });
            // Set TextView to Gone "No Results available. Start the EmptySeats detection."
            TextView noResultsTextView = findViewById(R.id.noResultsTextView);
            noResultsTextView.setVisibility(View.GONE);

        } else {
            MqttLogger.log("MQTT", "No screenshot available, placeholder will be shown.");
            Log.d("MQTT", "No screenshot available, placeholder will be shown.");
        }
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

        seatStatus = mqttService.getSeatStatus();
        if (seatStatus != null) {
            //Loop through each seat and update its color
            for (int i = 0; i < seatViews.length; i++) {
                if (seats[i] == 1) {
                    // Seat is occupied - set color to red
                    seatViews[i].setBackgroundColor(getResources().getColor(R.color.rot));
                } else {
                    // Seat is free - set color to green
                    seatViews[i].setBackgroundColor(getResources().getColor(R.color.grün));
                }
            }
        } else {
            MqttLogger.log("MQTT", "No seat status available.");
            Log.d("MQTT","No seat status available.");
        }

    }

    @Override
    protected void onStop() {
        // Unbind from the service
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        super.onStop();
    }
}