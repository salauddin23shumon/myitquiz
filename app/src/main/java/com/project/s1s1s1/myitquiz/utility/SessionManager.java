package com.project.s1s1s1.myitquiz.utility;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.project.s1s1s1.myitquiz.activity.LoginActivity;

public class SessionManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public Context context;
    private static final int MODE = Context.MODE_PRIVATE;
    private static final String PREF_NAME = "Session";
    private static final String LOGIN = "IS_LOGIN";
    private static final String NAME = "NAME";
    private static final String EMAIL = "EMAIL";
    private static final String ID = "ID";


    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String id, String name, String email) {
        editor.putBoolean(LOGIN, true);
        editor.putString(ID, id);
        editor.putString(NAME, name);
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public boolean checkLogin() {

        if (!this.isLogin()) {
            Intent i = new Intent(context, LoginActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity) context).finish();
            return true;
        }
        return false;
    }

    public void logout(final Class<? extends Activity> ActivityToBeOpen) {
        editor.clear();
        sharedPreferences.getBoolean(LOGIN, false);
        editor.commit();
        Intent i = new Intent(context, ActivityToBeOpen);
        context.startActivity(i);
        ((Activity) context).finish();
    }
}
