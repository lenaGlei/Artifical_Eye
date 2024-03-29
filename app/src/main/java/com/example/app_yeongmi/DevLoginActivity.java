package com.example.app_yeongmi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.app_yeongmi.databinding.ActivityDevloginBinding;

public class DevLoginActivity extends AppCompatActivity {

    ActivityDevloginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevloginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        // check if login fields are filled and check with database if valid
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();
                if(email.equals("")||password.equals(""))
                    Toast.makeText(DevLoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                    if(checkCredentials == true){
                        Toast.makeText(DevLoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(DevLoginActivity.this, main_settings.class);
                        intent.putExtra("EMAIL", email);
                        startActivity(intent);
                    }else{
                        Toast.makeText(DevLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //  Back button to navigate to the Login or Signup Activity
        ImageView imageViewBack = findViewById(R.id.btn_backSetting);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevLoginActivity.this, LoginOrSignup.class);
                startActivity(intent);
            }
        });
    }
}

