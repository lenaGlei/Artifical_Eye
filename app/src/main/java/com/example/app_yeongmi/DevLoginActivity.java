package com.example.app_yeongmi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DevLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devlogin);



        // login to next page
        Button button1 = findViewById(R.id.loginButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DeveloperUser2", "Button zurück clicked");
                Intent intent= new Intent(DevLoginActivity.this, DeveloperData.class);
                startActivity(intent);
            }
        });


    }


}

