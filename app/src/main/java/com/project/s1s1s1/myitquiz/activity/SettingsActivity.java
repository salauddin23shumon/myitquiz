package com.project.s1s1s1.myitquiz.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.project.s1s1s1.myitquiz.R;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences  sharedPreferences_sound;
    SharedPreferences.Editor edit_score, edit_sound;
    Switch  soundSW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.drawable.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        soundSW=findViewById(R.id.soundSW);

        sharedPreferences_sound = getSharedPreferences("Sound_Pref", Context.MODE_PRIVATE);
        edit_sound = sharedPreferences_sound.edit();

//        final Button sound = findViewById(R.id.play_sound);
//        Button reset = findViewById(R.id.reset);


        if (sharedPreferences_sound.getInt("Sound", 0) == 0) {
            soundSW.setChecked(false);
        } else
            soundSW.setChecked(true);

        soundSW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundSW.isChecked()) {
                    edit_sound.putInt("Sound", 1).apply();
                } else {
                    edit_sound.putInt("Sound", 0).apply();
                }
            }
        });

    }
}

