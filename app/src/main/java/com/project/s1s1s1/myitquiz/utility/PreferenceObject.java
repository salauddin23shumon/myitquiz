package com.project.s1s1s1.myitquiz.utility;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.project.s1s1s1.myitquiz.dataModel.User;

public class PreferenceObject {
    private Context context;
    private SharedPreferences preferences;

    public PreferenceObject(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("CachedLogin", Context.MODE_PRIVATE);
    }

    public void saveUserData(User user) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json=gson.toJson(user);
        editor.putString("user",json);
        editor.apply();
        /// creating session for the user
//        SessionManager sessionManager = new SessionManager(context);
//        sessionManager.createSession(user.getId(), user.getUserName(), user.getEmail());
    }


    public User getUserData(){
        Gson gson=new Gson();
        String json=preferences.getString("user","");
        return gson.fromJson(json,User.class);
    }

}
