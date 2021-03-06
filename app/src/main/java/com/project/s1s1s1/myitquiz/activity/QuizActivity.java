package com.project.s1s1s1.myitquiz.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.dataModel.QuizMenu;
import com.project.s1s1s1.myitquiz.dataModel.User;
import com.project.s1s1s1.myitquiz.utility.SessionManager;
import com.project.s1s1s1.myitquiz.utility.Constant;
import com.project.s1s1s1.myitquiz.utility.UserPreference;
import com.project.s1s1s1.myitquiz.utility.QuizViewAdapter;
import com.project.s1s1s1.myitquiz.utility.SyncData;
import static com.project.s1s1s1.myitquiz.utility.Utils.getBitmapImage;

public class QuizActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int MODE = Context.MODE_PRIVATE;
    private static final String TAG = "QuizActivity";
    private QuizViewAdapter adapter;
    private DrawerLayout drawerLayout;
    private TextView nav_header_name;
    private ImageView nav_header_image;
    private SessionManager sessionManager;
    private MediaPlayer mediaPlayer;
    private SharedPreferences sharedPreferences_sound;
    private User user;
    private UserPreference userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        adapter = new QuizViewAdapter(this, QuizMenu.getAllQuiz());
        viewConfig();
        userPreference = new UserPreference(this);
        user = userPreference.getUserData();
        setUserInfo(user);
        soundConfig();
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (mediaPlayer != null) {
//            outState.putInt("position", mediaPlayer.getCurrentPosition());
//            if (mediaPlayer.isPlaying())
//                mediaPlayer.pause();
//        }
//
//        Log.d(TAG, "onSaveInstanceState: called");
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        int pos = savedInstanceState.getInt("position");
//        if (mediaPlayer != null) {
//            mediaPlayer.seekTo(pos);
//        }
//        Log.d(TAG, "onRestoreInstanceState: " + pos);
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    private void soundConfig() {
        sharedPreferences_sound = getSharedPreferences("Sound_Pref", MODE);
        mediaPlayer = MediaPlayer.create(this, R.raw.piano_background);
        mediaPlayer.setLooping(true);
        //To play background sound
        if (sharedPreferences_sound.getInt("Sound", 0) == 0) {
            mediaPlayer.start();
        }
    }

    private void viewConfig() {
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        toolbar.setBackgroundResource(R.drawable.mytoolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        nav_header_name = header.findViewById(R.id.nav_header_name);
        nav_header_image = header.findViewById(R.id.nav_header_image);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        RecyclerView quizRV = findViewById(R.id.quizRV);
        quizRV.setHasFixedSize(true);
        quizRV.setLayoutManager(new GridLayoutManager(this, 2));
        quizRV.setAdapter(adapter);
    }

    private void setUserInfo(User mUser) {
        nav_header_name.setText(mUser.getUserName());
        Bitmap photo = getBitmapImage(mUser.getPhoto());
        Glide
                .with(QuizActivity.this)
                .load(photo)
                .into(nav_header_image);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_scorecard:
                startActivity(new Intent(this, ScoreCardActivity.class));
                break;
            case R.id.nav_Setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.nav_Help:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
            case R.id.nav_aboutUs:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.nav_logout:
                sessionManager.logout(LoginActivity.class);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sharedPreferences_sound.getInt("Sound", 0) == 0 && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (sharedPreferences_sound.getInt("Sound", 0) == 0)
            mediaPlayer.start();
        Log.d(TAG, "value: " + sharedPreferences_sound.getInt("Sound", 0));
        userPreference = new UserPreference(this);      //****updating user
        User newUser = userPreference.getUserData();
        setUserInfo(newUser);
        syncUserData(newUser);
    }

    @Override
    protected void onStart() {
        super.onStart();
        syncUserData(user);

    }

    private void syncUserData(User user) {
        int status = user.getSync_status();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (status == Constant.SYNC_STATUS_FAILED) {
                SyncData syncData = new SyncData(this);
                syncData.doSync();
            }
        }
    }
}
