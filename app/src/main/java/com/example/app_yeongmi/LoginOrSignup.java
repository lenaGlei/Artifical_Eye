package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginOrSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_signup);


        Button button1 = findViewById(R.id.btn_signup);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent= new Intent(LoginOrSignup.this, SignupActivity.class);
                startActivity(intent);
            }
        });


        Button button2 = findViewById(R.id.btn_login);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent= new Intent(LoginOrSignup.this, DevLoginActivity.class);
                startActivity(intent);
            }
        });

    }
}