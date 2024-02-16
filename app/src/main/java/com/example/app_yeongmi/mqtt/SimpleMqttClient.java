package com.example.app_yeongmi.mqtt;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.example.app_yeongmi.MqttLogger;
import com.hivemq.client.mqtt.MqttClientState;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
//import java.util.logging.Handler;
import android.os.Handler;

/**
 * A wrapper class for easier usage of HiveMQ mqtt library
 * änderung von activity zu context um einen services im hintergrund laufen zulassen und
 * run on Ui thread zu handler(looper.getMainLooper)
 */
public class SimpleMqttClient {

    //region Callback class definitions
    private static abstract class MqttOperationResult<T> implements BiConsumer<T, Throwable> {

        protected Context context;

        public MqttOperationResult(Context context) {
            this.context = context;
        }
        @Override
        public void accept(T ack, Throwable throwable) {
            if(throwable == null) {
                // success
                logSuccess();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess();
                    }
                });
            } else {
                //error
                logError(throwable);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onError(throwable);
                    }
                });
            }
        }

        protected abstract void logSuccess();

        protected abstract void logError(Throwable error);

        public void onSuccess() {
            // do nothing
        }

        public void onError(Throwable error) {
            // do nothing
        }
    }

    public static abstract class MqttConnection extends MqttOperationResult<Mqtt3ConnAck>{

        private Context context;

        public MqttConnection(Context context) {
            super(context);
        }

        @Override
        protected void logSuccess() {
            Log.d("MQTT", "Connection established");
        }

        @Override
        protected void logError(Throwable error) {
            Log.e("MQTT", "Unable to connect", error);
        }
    }

    public static abstract class MqttSubscription extends MqttOperationResult<Mqtt3SubAck> implements Consumer<Mqtt3Publish> {
        private final String topic;

        public String getTopic() {
            return topic;
        }

        public MqttSubscription(Context context, String topic) {
            super(context);
            this.topic = topic;
        }

        public abstract void onMessage(String topic, String payload);

        public abstract void onMessage(String topic, byte[] payload);

        @Override
        protected void logSuccess() {
            Log.d("MQTT", String.format("Subscribed to '%s'", topic));
        }

        @Override
        protected void logError(Throwable error) {
            Log.e("MQTT", String.format("Unable to subscribe to '%s'", topic), error);
        }

        @Override
        public void accept(Mqtt3Publish mqtt3Publish) {
            final String screenshotTopic = "emptySeats/ScreenshotToApp";
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    String topic = mqtt3Publish.getTopic().toString();
                    byte[] payloadBytes = mqtt3Publish.getPayloadAsBytes();

                    // Decides whether the message is processed as a string or byte array based on the topic
                    if (topic.equals(screenshotTopic)) {

                        // For the screenshot topic, the message is processed as a byte array
                        onMessage(topic, payloadBytes);
                    } else {
                        // For all other topics, the message is processed as a string
                        String payloadString = new String(payloadBytes);

                        onMessage(topic, payloadString);
                    }
                }
            });
        }
    }


    public static abstract class MqttPublish extends MqttOperationResult<Mqtt3Publish> {
        private final String topic;
        private final String payload;

        public String getTopic() {
            return topic;
        }

        public String getPayload() {
            return payload;
        }

        public MqttPublish(Context context, String topic, String payload) {
            super(context);
            this.topic = topic;
            this.payload = payload;
        }

        @Override
        protected void logSuccess() {
            Log.d("MQTT", String.format("Published to '%s':\n%s", topic, payload));
        }

        @Override
        protected void logError(Throwable error) {
            Log.e("MQTT", String.format("Unable to publish to '%s'", topic), error);
        }
    }
    //endregion

    //region Properties
    Mqtt3AsyncClient client;
    String serverHost;
    int serverPort;
    String identifier;

    public String getServerHost() {
        return serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean isConnected() {
        return client.getState() == MqttClientState.CONNECTED;
    }
    //endregion

    public SimpleMqttClient(String serverHost, int serverPort, String clientIdentifier) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.identifier = clientIdentifier;

        this.client = Mqtt3Client.builder().serverHost(serverHost).serverPort(serverPort).identifier(clientIdentifier).buildAsync();
    }

    //region MQTT service methods
    // Connect method (asynchronous)
    public void connect(MqttConnection conn) {
        this.client.connect().whenComplete(conn);
    }

    // Disconnect method (blocking)
    public void disconnect() {
        client.toBlocking().disconnect();
    }

    // Subscribe method (asynchronous)
    public void subscribe(MqttSubscription sub) {
        client.subscribeWith()
                .topicFilter(sub.getTopic())
                .callback(sub)
                .send()
                .whenComplete(sub);

    }

    // Unsubscribe method (blocking)
    public void unsubscribe(String topic) {
        client.toBlocking().unsubscribeWith().topicFilter(topic).send();
    }

    // Publish method (asynchronous)
    public void publish(MqttPublish pub) {
        client.publishWith()
                .topic(pub.getTopic())
                .qos(MqttQos.AT_LEAST_ONCE)
                .payload(pub.getPayload().getBytes())
                .send()
                .whenComplete(pub);
    }
    //endregion
}
