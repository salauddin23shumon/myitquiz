package com.project.s1s1s1.myitquiz.splashScreen;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import android.os.Bundle;

import android.view.Display;

import android.view.WindowManager;
import android.widget.FrameLayout;


import androidx.appcompat.app.AppCompatActivity;

import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.activity.ResultActivity;

import pl.droidsonroids.gif.GifImageView;


public class GameOverSplash extends AppCompatActivity {

    GifImageView gif1;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        gif1= findViewById(R.id.gifImageView1);
        mediaPlayer=MediaPlayer.create(this, R.raw.gameover3);

        mediaPlayer.start();

        /// code for screen orientation

        FrameLayout frameLayout = findViewById(R.id.frame);
        Resources res = getResources();
        Drawable portrait = res.getDrawable(R.drawable.black_bg);
        Drawable landscape = res.getDrawable(R.drawable.black_bg);

        WindowManager window = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        int num = display.getRotation();
        if (num == 0 || num == 2){
            frameLayout.setBackgroundDrawable(portrait);
        }else if (num == 1 || num == 3){
            frameLayout.setBackgroundDrawable(landscape);
        }

        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(8*999);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(GameOverSplash.this, ResultActivity.class);
                    startActivity(intent);
                    mediaPlayer.stop();
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
