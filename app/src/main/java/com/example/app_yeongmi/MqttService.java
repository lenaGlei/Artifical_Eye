package com.example.app_yeongmi;


//import static com.hivemq.client.internal.mqtt.util.MqttChecks.publish;


import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import android.widget.TextView;

import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import com.example.app_yeongmi.mqtt.SimpleMqttClient;
import com.example.app_yeongmi.mqtt.data.MqttMessage;

import org.json.JSONException;
import org.json.JSONObject;
public class MqttService extends Service {


    private SimpleMqttClient client;

    private Context context;
    private final String subscribeTopic = "emptySeats/HardwareToApp";
    private final String publishTopic = "emptySeats/AppToHardware";



    public MqttService() {
    }

    public class LocalBinder extends Binder {
        MqttService getService() {
            // Rückgabe dieser Instanz von MqttService, damit Clients öffentliche Methoden aufrufen können
            return MqttService.this;
        }
    }

    private final IBinder binder = new LocalBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        client = new SimpleMqttClient("broker.hivemq.com", 1883, UUID.randomUUID().toString());

        connect();

        Log.d("MQTT","Service onstartcommand connect");

        return START_REDELIVER_INTENT; //return START_NOT_STICKY; hier noch mal überlegen wassinnvoll ist
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }



    private void connect() {
        // establish connection to server (asynchronous)
        client.connect(new SimpleMqttClient.MqttConnection(this)
        {
            @Override
            public void onSuccess() {
                Toast.makeText(MqttService.this, "Connection successful", Toast.LENGTH_SHORT).show();
                Log.d("MQTT", "MQTT connection successful");
                subscribe(subscribeTopic);
                publish(publishTopic,"The app is successfully connected to MQTT and ready to receive information.");




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
                    Log.d("MQTT", "MQTT connection disconected");
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
                    messageArrived(topic,payload);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                // Logge den Fehler
                Log.e("MQTT", "Error in subscription", error);
            }
        });

    }

    public void messageArrived(String topic, String payload) throws Exception {
        if ("emptySeats/HardwareToApp".equals(topic)) {
            //String payload = new String(message.toString());
            Log.d("MQTT", "Nachricht erhalten: " + payload);

            Intent intent = new Intent("com.example.app.MQTT_MESSAGE");
            intent.putExtra("payload", payload);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }
    }





    private void sendMessageToMainActivity(String message) {
        Intent intent = new Intent("com.example.app_yeongmi.MQTT_MESSAGE");
        intent.putExtra("message", message);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }



    // Funktion zum Senden einer MQTT-Nachricht getApplicationContext(), topic) {
    //
    public void publish(String topic, String message) {
        // Überprüfen Sie, ob der Client verbunden ist
        if (client != null && client.isConnected()) {
            // Veröffentlichen Sie die Nachricht auf dem angegebenen Topic
            client.publish(new SimpleMqttClient.MqttPublish(getApplicationContext(),topic, message) {
                @Override
                public void onSuccess() {
                    // Nachricht erfolgreich veröffentlicht
                    Toast.makeText(MqttService.this, "MQTT Message send successful", Toast.LENGTH_SHORT).show();
                    Log.d("MQTT", "MQTT Message send successful");
                }

                @Override
                public void onError(Throwable error) {
                    // Fehler beim Veröffentlichen der Nachricht
                    Log.d("MQTT", "MQTT Message couldnt send");
                }
            });
        } else {
            Log.d("MQTT", "MQTT is not conected and Message couldnt send");
            // Der Client ist nicht verbunden, behandeln Sie diesen Fall entsprechend
        }
    }





}