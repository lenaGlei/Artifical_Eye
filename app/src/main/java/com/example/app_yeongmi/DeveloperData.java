package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;



import java.util.ArrayList;


public class DeveloperData extends AppCompatActivity {


    // for QR Code
    Button btn_QRCodeGenerate;
    ImageView img_qr;
    TextView brookerText;


    private MqttService mqttService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MqttService.LocalBinder binder = (MqttService.LocalBinder) service;
            mqttService = binder.getService();
            isBound = true;

            updateUIWithMqttSettings();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // LocalBinder to MqttService
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private void updateUIWithMqttSettings() {
        if (isBound) {
            TextView serverHostView = findViewById(R.id.ServerHost_insert);
            TextView serverPortView = findViewById(R.id.ServerPort_insert);
            TextView subTopicTextView = findViewById(R.id.subTopic_insert);
            TextView pubTopicTextView = findViewById(R.id.pubTopic_insert);
            TextView uuidTextView = findViewById(R.id.UUID_insert);
            TextView screenshotTopicView = findViewById(R.id.picTopic_insert);
            TextView navigationTopicView = findViewById(R.id.navTopic_insert);

            serverHostView.setText(mqttService.getServerHost());
            serverPortView.setText(mqttService.getServerPort());
            subTopicTextView.setText(mqttService.getSubscribeTopic());
            pubTopicTextView.setText(mqttService.getPublishTopic());
            uuidTextView.setText(mqttService.getClientIdentifier());
            screenshotTopicView.setText(mqttService.getScreenshotTopic());
            navigationTopicView.setText(mqttService.getNavigationTopic());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_data);

        //  Back button to navigate to the Main Settings screen
        ImageView imageViewBack = findViewById(R.id.btn_backMQTT);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeveloperData.this, main_settings.class);
                startActivity(intent);
            }
        });


        Button editTopicButton = findViewById(R.id.btn_editTopic);
        Button applyTopicButton = findViewById(R.id.btn_applyTopic);
        EditText subTopicText = findViewById(R.id.subTopic_insert);
        EditText pubTopicText = findViewById(R.id.pubTopic_insert);
        EditText picTopicText = findViewById(R.id.picTopic_insert);
        EditText navTopicText = findViewById(R.id.navTopic_insert);
        // Buttonpress "Edit Topics"
        editTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make EditText editable
                subTopicText.setEnabled(true);
                pubTopicText.setEnabled(true);
                picTopicText.setEnabled(true);
                navTopicText.setEnabled(true);

                // Show "Apply" button
                applyTopicButton.setVisibility(View.VISIBLE);
                editTopicButton.setVisibility(View.GONE);
            }
        });

        // Buttonpress Apply
        applyTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get Changes
                String newsubTopic = subTopicText.getText().toString();
                String newpubTopic = pubTopicText.getText().toString();
                String newpicTopic = picTopicText.getText().toString();
                String newnavTopic = navTopicText.getText().toString();

                // Update mqtt Service with new Topics
                mqttService.updateMqttSubscription(newsubTopic);
                mqttService.updateMqttPuplish(newpubTopic);
                mqttService.updatePictureSubscription(newpicTopic);
                mqttService.updateMqttNavigation(newnavTopic);

                // Deaktivate EdiText
                subTopicText.setEnabled(false);
                pubTopicText.setEnabled(false);
                picTopicText.setEnabled(false);
                navTopicText.setEnabled(false);

                // Show Edit Topic Button
                applyTopicButton.setVisibility(View.GONE);
                editTopicButton.setVisibility(View.VISIBLE);
            }
        });

        // Buttonpress to show qr code
        btn_QRCodeGenerate=findViewById(R.id.btn_QRCodeGenerate);
        img_qr=findViewById(R.id.img_qr);

        btn_QRCodeGenerate.setOnClickListener(v -> {
            generateQR();

        });
    }


    private void generateQR() {

        String text ="https://www.hivemq.com/demos/websocket-client/";
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            img_qr.setImageBitmap(bitmap);

            // Focus QR Code
            ScrollView scrollView = findViewById(R.id.scrollView);
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, img_qr.getTop());
                }
            });

        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

    }
}