package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.UUID;

import com.example.app_yeongmi.mqtt.SimpleMqttClient;
import com.example.app_yeongmi.mqtt.data.MqttMessage;

import org.json.JSONException;
import org.json.JSONObject;
public class MqttService extends Service {

    private SimpleMqttClient client;
    private Context context;
    private final String chatTopic = "emptySeats/testtopic1";
    public MqttService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        client = new SimpleMqttClient("broker.hivemq.com", 1883, UUID.randomUUID().toString());
        connect();
        Log.d("Mqtt","Service onstartcommand connect");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void connect() {
        // establish connection to server (asynchronous)
        client.connect(new SimpleMqttClient.MqttConnection(this)
        {
            @Override
            public void onSuccess() {
                Toast.makeText(MqttService.this, "Connection successful", Toast.LENGTH_SHORT).show();
                Log.d("MQTT", "MQTT connection successful");
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(MqttService.this, "Connection failed", Toast.LENGTH_SHORT).show();
                Log.d("MQTT", "MQTT connection faild");
            }
        });
    }

    @Override
    public void onDestroy() {
        // Trenne die MQTT-Verbindung, wenn der Service zerstört wird
        if (client != null && client.isConnected()) {
            if (client != null) {

                if (client.isConnected()) {
                    client.disconnect();
                }
            }
        }
        super.onDestroy();
    }


    private void subscribe(String topic) {
        // subscribe to topic (asynchronous)
        client.subscribe(new SimpleMqttClient.MqttSubscription(getApplicationContext(), topic) {
            @Override
            public void onMessage(String topic, String payload) {
                // Verarbeite die empfangene Nachricht
                try {
                    MqttMessage msg = deserializeMessage(payload);
                    // Hier kannst du z.B. einen Broadcast-Intent senden
                    Intent intent = new Intent("com.example.app_yeongmi.MQTT_MESSAGE");
                    intent.putExtra("topic", topic);
                    intent.putExtra("message", msg.getMessage());
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                } catch(JSONException je) {
                    Log.e("JSON", "Error while deserializing payload", je);
                    // Zeige den Fehler anders an, z.B. durch Loggen
                }
            }

            @Override
            public void onError(Throwable error) {
                // Logge den Fehler
                Log.e("MQTT", "Error in subscription", error);
            }
        });
    }

    private MqttMessage deserializeMessage(String json) throws JSONException {
        JSONObject jMessage = new JSONObject(json);

        String sUser = jMessage.getString("user");
        String sBody = jMessage.getString("t");

        MqttMessage newMsg = new MqttMessage(sUser, sBody);

        return newMsg;
    }
}