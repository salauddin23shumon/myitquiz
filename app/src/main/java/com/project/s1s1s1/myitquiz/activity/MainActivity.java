package com.project.s1s1s1.myitquiz.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;

import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.utility.SessionManager;
import com.project.s1s1s1.myitquiz.dataModel.User;
import com.project.s1s1s1.myitquiz.utility.Constant;
import com.project.s1s1s1.myitquiz.utility.SyncData;
import com.project.s1s1s1.myitquiz.utility.PreferenceObject;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SessionManager sessionManager;
    private SharedPreferences.Editor  editor_sound, editor_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences_login = getSharedPreferences("CachedLogin", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences_sound = getSharedPreferences("Sound_Pref", Context.MODE_PRIVATE);

        editor_login = sharedPreferences_login.edit();
        editor_sound = sharedPreferences_sound.edit();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        ImageButton btn_play = findViewById(R.id.btn_play);
        ImageButton btn_web = findViewById(R.id.button3);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();

            }
        });

        btn_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this, WebActivity.class);
                startActivity(intent);
                //finish();

            }
        });
    }

    // code  for menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.userporfile: {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.logout: {
                sessionManager.logout(LoginActivity.class);
                break;
            }

            case R.id.account:{
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                sessionManager.logout(RegisterActivity.class);
                                editor_login.clear().commit();
                                editor_sound.clear().commit();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                // custom alertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
                builder.setTitle(Html.fromHtml("<font color='#f71a03'>To create new account, your all previous data will be reset from the phone</font>"));
                builder.setMessage(Html.fromHtml("<font color='#04920A'>Are you sure want to create new account?</font>"));
                builder.setPositiveButton(Html.fromHtml("<font color='#000000'>Yes</font>"), dialogClickListener);
                builder.setNegativeButton(Html.fromHtml("<font color='#FA0707'>No</font>"), dialogClickListener);
                builder.create();
                builder.show();
                break;
            }
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceObject object=new PreferenceObject(this);
        User user = object.getUserData();
        int status = user.getSync_status();             // calling dataSync class
        Log.d(TAG, "onStart: status "+status);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (status == Constant.SYNC_STATUS_FAILED) {
                SyncData syncData = new SyncData(this);
                syncData.doSync();
            }
        }
    }
}
