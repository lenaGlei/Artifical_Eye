package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class languageSwitch extends AppCompatActivity {




    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String SWITCH_STATE = "switchState";
    private TextView textView;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_switch);




        final Switch mySwitch = findViewById(R.id.languageSwitch);
        textView=findViewById(R.id.textinput_placeholder);
        textView.setText("English");

        // Load the saved switch state
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        mySwitch.setChecked(settings.getBoolean(SWITCH_STATE, false));




        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the switch state
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(SWITCH_STATE, isChecked);
                editor.apply();
                if (isChecked){
                    textView.setText("Eingestellete Sprache: Deutsch");
                } else{
                    textView.setText("Language active: English");
                }
            }
        });


        Button button = findViewById(R.id.btn_backLanguage);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(languageSwitch.this, main_settings.class);
                startActivity(intent);
            }
        });





    }


}