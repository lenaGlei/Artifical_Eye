package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import android.util.Log;

import android.widget.Button;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.Locale;

public class EmptySeatsView extends AppCompatActivity {





    // Stühle eingabe von mqtt? hier?
    int[] seat = {1,1,0,1,0,1};

    ArrayList<Integer> seatsList;





    @Override
            protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_seats_view);

        seatsList = new ArrayList<Integer>();

        seatsList.add(R.id.seat1);
        seatsList.add(R.id.seat2);
        seatsList.add(R.id.seat3);
        seatsList.add(R.id.seat4);
        seatsList.add(R.id.seat5);
        seatsList.add(R.id.seat6);


        for (int i = 0; i < seat.length; i++) {

            int stuhlNummer = i;


            String seatString = "R.id.seat" + i;
            System.out.println(seatString);
            // int intSeat = Integer.parseInt(seatString);
            if (seat[i] == 1) {
                Button upperscreen = findViewById(seatsList.get(i));
                upperscreen.setBackgroundColor(Color.RED);


                Log.d("StuhlActivity", "Stuhl " + stuhlNummer + " ist belegt.");
            } else {
                Button upperscreen = findViewById(seatsList.get(i));
                upperscreen.setBackgroundColor(Color.GREEN);


                Log.d("StuhlActivity", "Stuhl " + stuhlNummer + " ist leer.");
            }
        }



    }










    }
