package com.project.s1s1s1.myitquiz.splashScreen;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.utility.SessionManager;
import com.project.s1s1s1.myitquiz.activity.LoginActivity;
import com.project.s1s1s1.myitquiz.activity.MainActivity;

import pl.droidsonroids.gif.GifImageView;

public class StartSplash extends AppCompatActivity {

    private SessionManager sessionManager;
    private Intent intent;
    private MediaPlayer mediaPlayer, mediaPlayer2;
    private GifImageView gifImageView1, gifImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_splash);

        gifImageView1 = findViewById(R.id.gifview1);
        gifImageView2 = findViewById(R.id.gifview2);
        sessionManager = new SessionManager(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.robot_intro);   //initializing
        mediaPlayer2 = MediaPlayer.create(this, R.raw.intro_app);
        mediaPlayer2.start();


        new Handler().postDelayed(new Runnable(){
            public void run() {
                gifImageView2.setVisibility(View.VISIBLE);
                mediaPlayer2.stop();
                mediaPlayer.start();
                gifImageView1.setVisibility(View.INVISIBLE);

            }
        }, 4 * 900);

        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(6 * 1000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (sessionManager.isLogin()) {    ///to check user already login or not
                        intent = new Intent(StartSplash.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        intent = new Intent(StartSplash.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    //finish();
                }
            }

        };
        th.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
