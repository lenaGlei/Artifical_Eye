package com.example.app_yeongmi;

import android.util.Log;

// Creates logs for Mqtt
public class MqttLogger {

    private static final StringBuilder logs = new StringBuilder();

    public static void log(String tag, String message) {
        if ("MQTT".equals(tag)) {
            Log.d("DebugCheck", "Log will be added: " + message);
            logs.append(message).append("\n");
        }
    }

    public static String getLogs() {
        return logs.toString();
    }
}
