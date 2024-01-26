package com.example.app_yeongmi;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import com.example.app_yeongmi.mqtt.SimpleMqttClient;
import com.example.app_yeongmi.mqtt.data.MqttMessage;


public class MainActivity extends AppCompatActivity {

    private static final String chatTopic = "emptySeats/testtopic1";
    private SimpleMqttClient client;
    private TextView txtTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new SimpleMqttClient("broker.hivemq.com", 1883, UUID.randomUUID().toString());


        // Sound
        MediaPlayer player= MediaPlayer.create(MainActivity.this,R.raw.sound1);
        player.start();


        Button button = findViewById(R.id.btn_start);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.release();

                Intent intent= new Intent(MainActivity.this, BlindUser2.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.btn_developer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent= new Intent(MainActivity.this, DeveloperUser2.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!client.isConnected()) {
            connect();
            subscribe(chatTopic);
        }
    }

    @Override
    protected void onStop() {
        // Überprüfe, ob der Client verbunden ist, bevor du versuchst, die Verbindung zu trennen

        if (client != null) {

            if (client.isConnected()) {
                client.disconnect();
            }
        }

        //client.unsubscribe(chatTopic);
        super.onStop();
    }

    private void connect() {
    // establish connection to server (asynchronous)
        client.connect(new SimpleMqttClient.MqttConnection(this) {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Connection successful", Toast.LENGTH_SHORT).show();
                Log.d("MQTT", "MQTT connection successful");
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                Log.d("MQTT", "MQTT connection faild");
            }
        });
    }

    private void subscribe(String topic) {
        // subscribe to chatTopic (asynchronous)
        client.subscribe(new SimpleMqttClient.MqttSubscription(this, chatTopic) {
            @Override
            public void onMessage(String topic, String payload) {
                // new message arrived
                // deserialize JSON into ChatMessage object
                try {
                    MqttMessage msg = deserializeMessage(payload);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String temp = msg.getMessage();
                            temp = temp.substring(0, Math.min(temp.length(), 5));
                            txtTemp.setText(String.format("Temperature: %s", temp));
                        }
                    });
                } catch(JSONException je) {
                    Log.e("JSON", "Error while deserializing payload", je);
                    showError("Invalid chat message received");
                }
            }

            @Override
            public void onError(Throwable error) {
                // error msg
            }
        });
    }

    private void showError(String msg) {
        Toast.makeText(this, String.format("Unexpected error %s. Check log for details", msg), Toast.LENGTH_LONG).show();
    }

    private MqttMessage deserializeMessage(String json) throws JSONException {
        JSONObject jMessage = new JSONObject(json);

        String sUser = jMessage.getString("user");
        String sBody = jMessage.getString("t");

        MqttMessage newMsg = new MqttMessage(sUser, sBody);

        return newMsg;
    }




}
