package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;



import java.util.ArrayList;


public class DeveloperData extends AppCompatActivity {

    // hier Mqtt array einfügen
    String[] dataArray = {"Item 1", "Item 2", "Item 3"};

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

            // Rufe hier die Daten ab
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
        // Verbinde dich mit dem MqttService
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
/*            TextView serverHostView = findViewById(R.id.ServerHost_insert);
            TextView serverPortView = findViewById(R.id.ServerPort_insert);
            TextView subTopicTextView = findViewById(R.id.subTopic_insert);
            TextView pubTopicTextView = findViewById(R.id.pubTopic_insert);
            TextView uuidTextView = findViewById(R.id.UUID_insert);



            serverHostView.setText(mqttService.getServerHost());
            serverPortView.setText(mqttService.getServerPort());
            subTopicTextView .setText(mqttService.getSubscribeTopic());
            pubTopicTextView .setText(mqttService.getPublishTopic());
            uuidTextView.setText(mqttService.getClientIdentifier());

 */
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_data);

        //btn_QRCodeGenerate=findViewById(R.id.btn_QRCodeGenerate);
        //img_qr=findViewById(R.id.img_qr);

        btn_QRCodeGenerate.setOnClickListener(v -> {
            generateQR();
        });


        /*

    // hier ändern zu link zu mqhive???
        brookerText= findViewById(R.id.Brooker_insert);


// Get references to the TextViews
        TextView brookerTextView = findViewById(R.id.Brooker_insert);
        TextView topicTextView = findViewById(R.id.Topic_insert);
        TextView uuidTextView = findViewById(R.id.UUID_insert);

// Set the text for each TextView based on the array
        brookerTextView.setText(dataArray[0]);
        topicTextView.setText(dataArray[1]);
        uuidTextView.setText(dataArray[2]);


         */




    }

    private void generateQR() {


        String text ="https://www.hivemq.com/demos/websocket-client/";
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            img_qr.setImageBitmap(bitmap);


        } catch (WriterException e) {
            throw new RuntimeException(e);
        }

    }




}