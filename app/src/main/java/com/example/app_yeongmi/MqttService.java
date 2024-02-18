package com.example.app_yeongmi;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.util.Objects;
import java.util.UUID;
import com.example.app_yeongmi.mqtt.SimpleMqttClient;
public class MqttService extends Service {

    private SimpleMqttClient client;
    private Context context;

    //MQTT-Settings
    private String serverHost = "broker.hivemq.com";
    private String clientIdentifier = UUID.randomUUID().toString();
    private int serverPort = 1883;
    // Topics
    private String subscribeTopic = "emptySeats/HardwareToApp";
    private String publishTopic = "emptySeats/AppToHardware";
    private String navigationTopic = "emptySeats/Navigation";
    private String screenshotTopic = "emptySeats/ScreenshotToApp";
    // to save and access in the settings
    private static Bitmap lastScreenshot = null;
    private int[] seatStatus;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    // Getter and Setter for Settings DeveloperData and DetectionDashboard
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
    public void setSubscribeTopic(String subscribeTopic){ this.subscribeTopic = subscribeTopic;}
    public String getScreenshotTopic() {
        return screenshotTopic;
    }
    public String getPublishTopic() {
        return publishTopic;
    }
    public String getNavigationTopic() {
        return navigationTopic;
    }
    public int[] getSeatStatus() { return seatStatus; }
    public void setSeatStatus(int[] seatStatus) { this.seatStatus = seatStatus; }
    public static Bitmap getLastScreenshot() {
        return lastScreenshot;
    }


    public MqttService() {
    }

    // LocalBinder for activities to bind to the MqttService.
    // Allows other activitys to access the methods from the MqttService.
    public class LocalBinder extends Binder {
        MqttService getService() {
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

        return START_REDELIVER_INTENT;
    }



    private void connect() {
        // establish connection to server
        client.connect(new SimpleMqttClient.MqttConnection(this)
        {
            @Override
            public void onSuccess() {
                Toast.makeText(MqttService.this, "Connection successful", Toast.LENGTH_SHORT).show();
                Log.d("MQTT", "MQTT connection successful");
                MqttLogger.log("MQTT","MQTT connection successful");

                subscribe(subscribeTopic);
                subscribe(screenshotTopic);
                subscribe(navigationTopic);
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
        // Disconnect the MQTT connection
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
        // subscribe to topic
        client.subscribe(new SimpleMqttClient.MqttSubscription(getApplicationContext(), topic) {

            @Override
            public void onSuccess(){
                MqttLogger.log("MQTT", String.format("Subscribed to '%s'", topic));

            }

            // onMessage for String
            @Override
            public void onMessage(String topic, String payload) {
                Log.d("MQTT", String.format("Received message from topic '%s':\n%s", topic, payload));
                MqttLogger.log("MQTT", String.format("Received message from topic '%s':\n%s", topic, payload));

                if(Objects.equals(topic, subscribeTopic)) {
                    try {
                        // for BroadcastReceiver in EmptyView and Cybathlon Acticty
                        Intent intent = new Intent("com.example.app.MQTT_MESSAGE");
                        intent.putExtra("payload", payload);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                if(Objects.equals(topic, navigationTopic)) {
                    try {
                        // for BroadcastReceiver in EmptyView and Cybathlon Acticty
                        Intent intent = new Intent("com.example.app.MQTT_NAVIGATION");
                        intent.putExtra("payload", payload);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            // onMessage for screenshot payload=byte[]
            @Override
            public void onMessage(String topic, byte[] payload) {
                try {
                    Log.d("MQTT", String.format("Received screenshot from topic '%s'", topic));
                    MqttLogger.log("MQTT", String.format("Received screenshot from topic '%s'", topic));
                    // Convert the byte array into an image (bitmap)
                    Bitmap bitmap = BitmapFactory.decodeByteArray(payload, 0, payload.length);
                    lastScreenshot = bitmap;

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                Log.e("MQTT", "Error in subscription", error);
            }
        });

    }

    // Sending an MQTT message
    public void publish(String topic, String message) {
        // Check if the client is connected
        if (client != null && client.isConnected()) {
            // Publish the message on the topic
            client.publish(new SimpleMqttClient.MqttPublish(getApplicationContext(),topic, message) {
                @Override
                public void onSuccess() {
                    // Message published
                    Toast.makeText(MqttService.this, "MQTT Message send successful", Toast.LENGTH_SHORT).show();
                    MqttLogger.log("MQTT",String.format("Published to '%s':\n%s", topic, message));
                }

                @Override
                public void onError(Throwable error) {
                    // Message could not be published
                    Log.d("MQTT", "MQTT Message could not be published");
                }
            });
        } else {
            Log.d("MQTT", "MQTT is not conected and Message couldnt send");
        }
    }

    //For edit Topics in Developerdata
    void updateMqttSubscription(String newTopic) {
        // only update if the topic has changed
        if(!Objects.equals(subscribeTopic, newTopic)) {
            client.unsubscribe(subscribeTopic);
            subscribe(newTopic);
            subscribeTopic=newTopic;
        }

    }
    void updateMqttNavigation(String newTopic) {
        // only update if the topic has changed
        if(!Objects.equals(navigationTopic, newTopic)) {
            client.unsubscribe(navigationTopic);
            subscribe(newTopic);
            navigationTopic=newTopic;
        }

    }
    void updateMqttPuplish(String newTopic) {
        publishTopic=newTopic;
    }

    void updatePictureSubscription(String newTopic) {
        // only update if the topic has changed
        if(!Objects.equals(screenshotTopic, newTopic)) {
            client.unsubscribe(screenshotTopic);
            subscribe(newTopic);
            screenshotTopic = newTopic;
        }
    }
}