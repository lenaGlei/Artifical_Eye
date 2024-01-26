package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;
import java.util.UUID;

public class EmptySeatsView extends AppCompatActivity {

    //private Button btn;
   // private static final String TAG = "EmptySeatsView";
   // private String topic, clientID;
    //private MqttAndroidClient client;
    private TextToSpeech textToSpeech;


    // Stühle eingabe von mqtt? hier?
    int[] seat = {1,1,0,1,0,1};



    @Override
            protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_seats_view);
    }/*
// Stühle eingabe von mqtt? hier?
        int[] seat = {1,1,0,1,0,1};


        //init();

        onStart();


        // Audio output sitze
        //textToSpeech = new TextToSpeech(this, this);
        pruefeSitzStatus(seat);
    }




    private void pruefeSitzStatus(int[] seat) {
        StringBuilder ausgabe = new StringBuilder();

        for (int i = 0; i < seat.length; i++) {
            if (seat[i] == 1) {
                ausgabe.append("Sitzplatz ").append(i + 1).append(" ist frei. ");
            } else {
                ausgabe.append("Sitzplatz ").append(i + 1).append(" ist besetzt. ");
            }
        }

        sprecheText(ausgabe.toString());
    }


    private void sprecheText(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage();

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Die Sprache wird nicht unterstützt.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Text-to-Speech-Initialisierung fehlgeschlagen.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart( ) {
        super.onStart();
        for (int i = 0; i < seat.length; i++) {

            int stuhlNummer = i + 1;
            String seatString = "R.id.seat" + i;
            System.out.println(seatString);
            int intSeat = Integer.parseInt(seatString);
            if (seat[i] == 1) {
                LinearLayout upperscreen = findViewById(intSeat);
                upperscreen.setBackgroundColor(Color.RED);


                Log.d("StuhlActivity", "Stuhl " + stuhlNummer + " ist belegt.");
            } else {
                LinearLayout upperscreen = findViewById(intSeat);
                upperscreen.setBackgroundColor(Color.GREEN);


                Log.d("StuhlActivity", "Stuhl " + stuhlNummer + " ist leer.");
            }
        }
    }


    private void init() {
        btn =findViewById(R.id.btn_CybathlonActive);
        clientID = "clientId-yapzkhkRsy";
        topic = "Seats";
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://broker.hivemq.com;1883", clientID);
      btn.setOnClickListener(new View.OnClickListener(){

          @Override
          public void onClick(View v){
              connectX();
          }
      });

    }

    private void connectX ()     {

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    sub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }



    private void sub() {
        try {
            client.subscribe(topic, 0);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //log

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d(TAG, "topic" + topic);
                    Log.d(TAG, "message:" + new String(message.getPayload()));



                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //toast or log

                }
            });
        } catch (MqttException e) {

        }
    }

  */








    //SImple MqttClient
    /*

    private SimpleMqttClient simpleMqttClient;
    client = new SimpleMqttClient("broker.hivemq.com", 1883,UUID.randomUUID().toString());
    private String chatTopic = "freeSeats"; // Change this to your MQTT topic

    private seat1 seat1;
    private seat2 seat2;
    private seat3 seat3;
    private seat4 seat4;
    private seat5 seat5;
    private seat6 seat6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_seats_view);

        // Connect UI elements
        seat1 = findViewById(R.id.seat1);
        seat2 =findViewById(R.id.seat2);
        seat3 =findViewById(R.id.seat3);
        seat4 =findViewById(R.id.seat4);
        seat5 =findViewById(R.id.seat5);
        seat6 =findViewById(R.id.seat6);

        connect()

    }

    private void receiveMessage(String payload) {
        // Deserialize JSON into MqttMessage object
        try {
            MqttMessage msg = MqttHelper.deserializeMessage(payload);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayout upperscreen = findViewById(R.id.seat1);
                    if(msg== 1) upperscreen.setBackgroundColor(Color.RED);
                    else if(msg == 0) upperscreen.setBackgroundColor(Color.GREEN);
                }
            });
        } catch (JSONException je) {
            Log.e("JSON", "Error while deserializing payload", je);
            showError("Invalid message received");
        }
    }

    private void showError(String msg) {
        Toast.makeText(MainActivity.this, String.format("Unexpected error: %s. Check the log for details", msg), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        // Disconnect from MQTT service when the activity is destroyed
        mqttHelper.disconnect();
        super.onDestroy();
    }


     */

/*

//Mqtt handler
    private static final String BROOKER_URL = "https://www.hivemq.com/demos/websocket-client/";
    private static final String CLIENT_ID = "clientId-QsPhLuPXXs";
    private MqttHandler mqttHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_seats_view);

        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROOKER_URL,CLIENT_ID);

    }

    @Override
    protected void onDestroy() {
        mqttHandler.disconnect();
        super.onDestroy();

    }
    private void publishMessage(String topic, String message){
        Toast.makeText(this, "Publishing message: " + message, Toast.LENGTH_SHORT).show();
        mqttHandler.publish(topic,message);
    }
    private void subscribeToTopic(String topic){
        Toast.makeText(this, "Subscribing to topic "+ topic, Toast.LENGTH_SHORT).show();
        mqttHandler.subscribe(topic);
    }

 */






/*
        // change colour if seat is free --> depending on array for every seat!

        LinearLayout upperscreen = findViewById(R.id.seat1);
        if(numberSeat == 1) upperscreen.setBackgroundColor(Color.RED);
        else if(numberSeat == 0) upperscreen.setBackgroundColor(Color.GREEN);

        LinearLayout upperscreen = findViewById(R.id.seat2);
        if(numberSeat == 1) upperscreen.setBackgroundColor(Color.RED);
        else if(numberSeat == 0) upperscreen.setBackgroundColor(Color.GREEN);

        LinearLayout upperscreen = findViewById(R.id.seat3);
        if(numberSeat == 1) upperscreen.setBackgroundColor(Color.RED);
        else if(numberSeat == 0) upperscreen.setBackgroundColor(Color.GREEN);

        LinearLayout upperscreen = findViewById(R.id.seat4);
        if(numberSeat == 1) upperscreen.setBackgroundColor(Color.RED);
        else if(numberSeat == 0) upperscreen.setBackgroundColor(Color.GREEN);

        LinearLayout upperscreen = findViewById(R.id.seat5);
        if(numberSeat == 1) upperscreen.setBackgroundColor(Color.RED);
        else if(numberSeat == 0) upperscreen.setBackgroundColor(Color.GREEN);

        LinearLayout upperscreen = findViewById(R.id.seat6);
        if(numberSeat == 1) upperscreen.setBackgroundColor(Color.RED);
        else if(numberSeat == 0) upperscreen.setBackgroundColor(Color.GREEN);

*/





    }
