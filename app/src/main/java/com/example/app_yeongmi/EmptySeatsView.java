package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import android.util.Log;

import android.widget.LinearLayout;


import java.util.Locale;

public class EmptySeatsView extends AppCompatActivity {





    // Stühle eingabe von mqtt? hier?
    int[] seat = {1,1,0,1,0,1};



    @Override
            protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_seats_view);



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








    }
