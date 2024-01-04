package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cybathlon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cybathlon);


        Button button = findViewById(R.id.btn_CybathlonActive);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent= new Intent(Cybathlon.this, EmptySeatsView.class);
                startActivity(intent);
            }
        });


    }
}