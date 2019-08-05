package com.project.s1s1s1.myitquiz.splashScreen;

import android.content.Intent;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.activity.ResultActivity;

public class LevelCompletedSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_completed);

        TextView myText = (TextView) findViewById(R.id.mytext );

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        myText.startAnimation(anim);

        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(4*1000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(LevelCompletedSplash.this, ResultActivity.class);
                    startActivity(intent);
                    //finish();
                }
            }

        };
        th.start();

    }

    @Override
    public void onBackPressed() { }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
