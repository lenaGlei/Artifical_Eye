package com.example.app_yeongmi;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttHandler {

    private MqttAndroidClient client;
    private String brokerUrl;
    private String clientId;

    public MqttHandler(String brokerUrl, String clientId) {
        this.brokerUrl = brokerUrl;
        this.clientId = clientId;
    }

    public void connect(Context context) {
        try {
            client = new MqttAndroidClient(context, brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            client.connect(options);

            Log.d("MqttHandler","Connected to MQTT Broker: " + brokerUrl);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d("MqttHandler","Could not connect to MQTT Broker: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (client != null) {
                client.disconnect();
                Log.d("MqttHandler","Disconnected from MQTT Broker");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        return client != null && client.isConnected();
    }

    public void publish(String topic, String message, int qos) {
        if(!isConnected()){
            Log.d("MqttHandler","Client not yet connected");
            return;
        }
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(qos);
            client.publish(topic, mqttMessage);
            Log.d("MqttHandler","Message published to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic, int qos) {
        if(!isConnected()){
            Log.d("MqttHandler","Client not yet connected");
            return;
        }
        try {
            client.subscribe(topic, qos);
            Log.d("MqttHandler","Subscribed to topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}

