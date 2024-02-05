package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        TextView scrollableTextView = findViewById(R.id.instruction_insert);

        // Ressource aus raw-Ordner laden
        InputStream inputStream = getResources().openRawResource(R.raw.readmeapp);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            // Fehlerbehandlung
            e.printStackTrace();
        }

        // Setze den Text im TextView
        scrollableTextView.setText(byteArrayOutputStream.toString());

        ImageView imageViewBack = findViewById(R.id.btn_backSetting);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Instructions.this, main_settings.class);
                startActivity(intent);
            }
        });

    }

}