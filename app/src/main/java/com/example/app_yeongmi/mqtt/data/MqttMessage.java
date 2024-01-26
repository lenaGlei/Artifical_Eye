package com.example.app_yeongmi.mqtt.data;

public class MqttMessage {
    private final String user;
    private final String message;

    public MqttMessage(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}