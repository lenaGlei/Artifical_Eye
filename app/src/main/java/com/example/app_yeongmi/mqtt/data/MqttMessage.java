package com.example.app_yeongmi.mqtt.data;

public class MqttMessage {
    private final String user;
    private final String message;
    private final byte[] binaryData;

    public MqttMessage(String user, String message) {
        this.user = user;
        this.message = message;
        this.binaryData = null;
    }

    //für screenshot erweitert
    public MqttMessage(String user, byte[] binaryData) {
        this.user = user;
        this.message = null;
        this.binaryData = binaryData;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public boolean isBinary() {
        return binaryData != null;
    }
}