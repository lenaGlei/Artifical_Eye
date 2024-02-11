package com.example.app_yeongmi;


//import static com.hivemq.client.internal.mqtt.util.MqttChecks.publish;


import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import android.widget.TextView;

import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import com.example.app_yeongmi.mqtt.SimpleMqttClient;
import com.example.app_yeongmi.mqtt.data.MqttMessage;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;
public class MqttService extends Service {


    private SimpleMqttClient client;

    private Context context;
    private String subscribeTopic = "emptySeats/HardwareToApp";

    private String screenshotTopic = "emptySeats/ScreenshotToApp";
    private static Bitmap lastScreenshot = null;
    private String publishTopic = "emptySeats/AppToHardware";

    //MQTT-Einstellungen
    private String serverHost = "broker.hivemq.com";
    private String clientIdentifier = UUID.randomUUID().toString();
    private int serverPort = 1883;

    private int[] seatStatus;




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
        client = new SimpleMqttClient(serverHost, serverPort ,clientIdentifier);

        connect();

        Log.d("MQTT","Service onstartcommand connect");

        return START_REDELIVER_INTENT; //return START_NOT_STICKY; hier noch mal überlegen wassinnvoll ist
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // Methoden, um die Einstellungen abzurufen
    public String getServerHost() {
        return serverHost;
    }

    public String getServerPort() {
        return String.valueOf(serverPort);
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }
    public String getSubscribeTopic() {
        return subscribeTopic;
    }
    public String getScreenshotTopic() {
        return screenshotTopic;
    }
    public String getPublishTopic() {
        return publishTopic;
    }

    public int[] getSeatStatus() { return seatStatus; }
    public void setSeatStatus(int[] seatStatus) { this.seatStatus = seatStatus; }

    private void connect() {
        // establish connection to server (asynchronous)
        client.connect(new SimpleMqttClient.MqttConnection(this)
        {
            @Override
            public void onSuccess() {
                Toast.makeText(MqttService.this, "Connection successful", Toast.LENGTH_SHORT).show();
                Log.d("MQTT", "MQTT connection successful");
                MqttLogger.log("MQTT","MQTT connection successful");
                subscribe(subscribeTopic);
                testScreenshot();
                subscribe(screenshotTopic);
                publish(publishTopic,"The app is successfully connected to MQTT and ready to receive information.");

            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(MqttService.this, "Connection failed", Toast.LENGTH_SHORT).show();
                Log.d("MQTT", "MQTT connection failed");
                MqttLogger.log("MQTT","MQTT connection failed");
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
                    MqttLogger.log("MQTT","MQTT connection disconected");
                }
            }
        }
        super.onDestroy();
    }


    private void subscribe(String topic) {
        // subscribe to topic (asynchronous)
        client.subscribe(new SimpleMqttClient.MqttSubscription(getApplicationContext(), topic) {

            @Override
            public void onSuccess(){
                MqttLogger.log("MQTT",String.format("Subscribed to '%s'", topic));
            }
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

    // für BroadcastReceiver im EmptyViewActivity
    public void messageArrived(String topic, String payload) throws Exception {
        if (subscribeTopic.equals(topic)) {


            MqttLogger.log("MQTT",String.format("Received message from topic '%s':\n%s", topic, payload));

            Intent intent = new Intent("com.example.app.MQTT_MESSAGE");
            intent.putExtra("payload", payload);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }

        if (screenshotTopic.equals(topic)) {
            byte[] payloadArray = payload.getBytes();
            lastScreenshot = BitmapFactory.decodeByteArray(payloadArray, 0, payloadArray.length);

            // Send broadcast
            Intent intent = new Intent("emptySeats/ScreenshotReceived");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }

    // Methode zum Abrufen des letzten Screenshots
    public static Bitmap getLastScreenshot() {
        return lastScreenshot;
    }

    public void testScreenshot() {
        // Lade das Bitmap aus den Ressourcen
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.jonnabild);

        // Konvertiere das Bitmap in ein Byte-Array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // Veröffentliche das Byte-Array auf dem MQTT Topic
        //screenshotpublish(screenshotTopic, byteArray);
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
                    MqttLogger.log("MQTT",String.format("Published to '%s':\n%s", topic, message));
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

    void updateMqttSubscription(String newTopic) {
        client.unsubscribe(subscribeTopic);
        subscribe(newTopic);
        subscribeTopic=newTopic;

    }

    void updateMqttPuplish(String newTopic) {
        publishTopic=newTopic;

    }



}