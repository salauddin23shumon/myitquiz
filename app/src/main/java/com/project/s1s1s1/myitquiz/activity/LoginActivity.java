package com.project.s1s1s1.myitquiz.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.utility.SessionManager;
import com.project.s1s1s1.myitquiz.dataModel.Score;
import com.project.s1s1s1.myitquiz.dataModel.User;
import com.project.s1s1s1.myitquiz.utility.Constant;
import com.project.s1s1s1.myitquiz.utility.PreferenceObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.net.*;
import java.util.concurrent.ExecutionException;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;
import static com.project.s1s1s1.myitquiz.utility.Utils.getStringImage;
import static com.project.s1s1s1.myitquiz.utility.Utils.isNetworkAvailable;
import static com.project.s1s1s1.myitquiz.utility.VolleyResponse.errorResponse;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText ed_name, ed_password;
    private Button btn_login, btn_signup;
    private ProgressBar loading;
    private String sName, sEmail, sId, sPhoto, name, password;
    String errMsg = " Login Failed";
    private CheckBox chkb;
    MediaPlayer error_beep, ok_beep;
    Score userScore;
    private static final String URL_LOGIN = "api_login.php";
    PreferenceObject userObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userObject = new PreferenceObject(this);

        loading = findViewById(R.id.loading);
        ed_name = findViewById(R.id.name);
        ed_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_regist);

        error_beep = MediaPlayer.create(this, R.raw.error_all);
        ok_beep = MediaPlayer.create(this, R.raw.succeess);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        chkb = findViewById(R.id.checkBox);
// code for checkbox
        chkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else
                    ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
    }

    private void login() {

        initialize();
        if (!validate()) {
            Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
            error_beep.start();
        } else {
            loading.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.GONE);

            if (isNetworkAvailable(LoginActivity.this)) {
                jsonTask();
            } else {  ///offline login for the last user
                offlineLogin();
            }
        }
    }

    private void offlineLogin() {
        if (userObject.getUserData() != null) {
            User user = userObject.getUserData();
            String spName = user.getUserName();
            String spId = user.getId();
            String spPassword = user.getPassword();
            String spMail = user.getEmail();

            if (name.equals(spName) && password.equals(spPassword)) {

                createSession(spId, spName, spMail);
                afterLoginSuccess();
            } else {
                // username / password doesn't match&
                afterLoginFail("Username/Password is incorrect");
            }
        } else {
            afterLoginFail("You have no Account. Please Create an Account");
        }
    }

    private void afterLoginSuccess() {
        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);
        ok_beep.start();
        resetEditor();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
        finish();
    }

    private void initialize() {
        name = ed_name.getText().toString().trim();
        password = ed_password.getText().toString().trim();
    }

    ///input validation
    private boolean validate() {

        boolean valid = true;
        if (name.isEmpty() || name.length() > 32) {
            ed_name.setError("Enter valid name");
            valid = false;
        } else
            ed_name.setError(null);

        if (password.isEmpty()) {
            ed_password.setError("Please Enter Password");
            valid = false;
        } else ed_password.setError(null);
        return valid;
    }

    //////accessing server via volley and json
    private void jsonTask() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (result.equals("1")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    sName = object.getString("username");
                                    sEmail = object.getString("email");
                                    sId = object.getString("id");
                                    Log.e(TAG, "login: " + sId);
                                    createSession(sId, sName, sEmail);
                                    sPhoto = object.getString("photo"); ///not exactly an myImage... it is the url of myImage

                                    int computer = object.getInt("Computer");
                                    int mysql = object.getInt("MySql");
                                    int php = object.getInt("PHP");
                                    int cProg = object.getInt("C_prog");
                                    int cpp = object.getInt("CPP");
                                    int cs = object.getInt("cSharp");
                                    int ds = object.getInt("DS");
                                    int css = object.getInt("CSS");
                                    int js = object.getInt("JS");
                                    int html = object.getInt("HTML");
                                    int java = object.getInt("java");
                                    int dld = object.getInt("DLD");
                                    Log.e(TAG, "onResponse: " + jsonArray.length());
                                    userScore = new Score(cpp, cs, cProg, java, html, ds, dld, css, js, computer, mysql, php);

                                    try {
                                        Bitmap photo = new DownloadImage().execute(sPhoto).get(); ///calling class
                                        saveData(photo);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {//json data mismatch
                                afterLoginFail(jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            afterLoginFail(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        afterLoginFail(errorResponse(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", name);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveData(Bitmap photo) {
        PreferenceObject object = new PreferenceObject(this);
        String stringImage = getStringImage(photo);
        User user = new User(sId, sName, sEmail, password, stringImage, userScore);
        object.saveUserData(user);
        afterLoginSuccess();
    }

    private void afterLoginFail(String errMsg) {
        loading.setVisibility(View.GONE);
        btn_login.setVisibility(View.VISIBLE);
        error_beep.start();
        Toast.makeText(LoginActivity.this, "Error!!! " + errMsg, Toast.LENGTH_SHORT).show();
    }

    private void resetEditor() {
        /// editor clearing
        ed_name.setText("");
        ed_password.setText("");
        ed_name.setFocusableInTouchMode(true);
        ed_name.requestFocus();
    }


    /// class for myImage downloading from url and saving into sharedPreferences
    public static class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream in = new URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
        }
    }

    private void createSession(String id, String name, String email) {
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.createSession(id, name, email);
    }
}
