package com.example.app_yeongmi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

public class languageSwitch extends AppCompatActivity {


    private static String PREFS_NAME = "MyPrefsFile";
    private static String SWITCH_STATE = "switchState";
    private TextView textView;
    SwitchCompat mySwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_switch);



        // Create shared preferences. Default setting is english.
        mySwitch = (SwitchCompat) findViewById(R.id.languageSwitchbtn);
        textView=findViewById(R.id.textinput_placeholder);
        textView.setText("Language active: English");
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the switch state
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(SWITCH_STATE, isChecked);
                editor.apply();
                if (isChecked){
                    textView.setText("Active Sprache: Deutsch");
                } else{
                    textView.setText("Language active: English");
                }
            }
        });


        View button2 = findViewById(R.id.btn_backLanguage);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(languageSwitch.this, main_settings.class);
                startActivity(intent);
            }
        });

    }

}