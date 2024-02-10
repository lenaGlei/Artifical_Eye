package com.example.app_yeongmi;

import android.util.Log;


public class MqttLogger {

    private static final StringBuilder logs = new StringBuilder();

    public static void log(String tag, String message) {
        if ("MQTT".equals(tag)) {
            Log.d("DebugCheck", "Log wird hinzugefügt: " + message);
            logs.append(message).append("\n");
            // Optional: Benachrichtigen Sie die UI oder einen Listener, dass ein neues Log hinzugefügt wurde
        }
    }

    public static String getLogs() {
        return logs.toString();
    }
}
