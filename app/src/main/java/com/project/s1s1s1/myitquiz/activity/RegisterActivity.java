package com.project.s1s1s1.myitquiz.activity;


import android.content.Intent;
import android.graphics.Bitmap;

import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.dataModel.Score;
import com.project.s1s1s1.myitquiz.dataModel.User;
import com.project.s1s1s1.myitquiz.utility.Constant;
import com.project.s1s1s1.myitquiz.utility.PreferenceObject;
import com.project.s1s1s1.myitquiz.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.project.s1s1s1.myitquiz.utility.Utils.chooseImageFile;
import static com.project.s1s1s1.myitquiz.utility.Utils.getBitmapImage;
import static com.project.s1s1s1.myitquiz.utility.Utils.getSound;
import static com.project.s1s1s1.myitquiz.utility.Utils.getStringImage;
import static com.project.s1s1s1.myitquiz.utility.VolleyResponse.errorResponse;


public class RegisterActivity extends AppCompatActivity {

    private static final int PHOTO_REQUEST_CODE = 1;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText ed_name, ed_email, ed_password;
    private String id, name, email, password, image, error_stmt = "SignUp has Failed";
    private User user;
    private Button btn_regist, btn_photo;
    private ProgressBar loading;
    private CircleImageView profile_image;
    private SessionManager sessionManager;
    private static final String URL_REGISTER = "api_signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sessionManager = new SessionManager(this);

        loading = findViewById(R.id.loading);
        ed_name = findViewById(R.id.name);
        ed_email = findViewById(R.id.email);
        ed_password = findViewById(R.id.password);
        btn_regist = findViewById(R.id.btn_regist);
        btn_photo = findViewById(R.id.btn_photo);
        profile_image = findViewById(R.id.profile_image);
        CheckBox chkb = findViewById(R.id.checkBox);


        chkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else
                    ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFile(RegisterActivity.this, PHOTO_REQUEST_CODE);  //<-chooseImageFile called
            }
        });
    }//onCreate finish

    private void initialize() {
        name = ed_name.getText().toString().trim();
        email = ed_email.getText().toString().trim();
        password = ed_password.getText().toString().trim();
    }

    private void register() {
        initialize();
        if (!validate()) {
            Toast.makeText(this, error_stmt, Toast.LENGTH_SHORT).show();
            getSound(this,0).start();
        } else {
            jsonTask();
        }
    }

    // this method called by chooseImageFile method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                RequestOptions myOptions = new RequestOptions()
                        .centerCrop() // or centerCrop
                        .override(190, 190);

                Glide
                        .with(RegisterActivity.this)
                        .asBitmap()
                        .apply(myOptions)
                        .load(bitmap)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                image = (getStringImage(resource));
                                profile_image.setImageBitmap(getBitmapImage(image));
                            }
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void jsonTask() {
        final PreferenceObject object = new PreferenceObject(this);
        loading.setVisibility(View.VISIBLE);
        btn_regist.setVisibility(View.GONE);
        error_stmt = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                JSONObject jsonObject = new JSONObject(response);
                                String result = jsonObject.getString("success");

                                if (result.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "New Account Created", Toast.LENGTH_SHORT).show();

                                    getSound(RegisterActivity.this,1).start();
                                    id = jsonObject.getString("message").trim(); /// getting id from db via api
                                    Log.d(TAG, "id: " + id);
                                    user = new User(id, name, email, password, image, userScore());
                                    createSession(id, name, email);
                                    object.saveUserData(user);

                                    resetEditor();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    afterLoginFail(jsonObject.getString("message"));
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "Please check Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            afterLoginFail(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                afterLoginFail(errorResponse(error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", name);
                params.put("email", email);
                params.put("password", password);
                params.put("photo", image);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }///    jsonTask close  /////

    private void createSession(String id, String name, String email) {
        sessionManager.createSession(id, name, email);
    }

    private Score userScore() {
        return new Score(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    private void resetEditor() {
        ///resetting editor
        profile_image.setImageResource(android.R.color.transparent);
        ed_name.setFocusable(true);
        ed_name.requestFocus();
        ed_name.setText("");
        ed_email.setText("");
        ed_password.setText("");
    }

    private boolean validate() {

        boolean valid = true;
        if (name.isEmpty() || name.length() > 32) {
            ed_name.setError("Please Enter valid name");
            valid = false;
        } else if (!name.matches("[a-z A-Z0-9.@]*")) {
            ed_name.setError("Please enter valid name without special symbol");
            valid = false;
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_email.setError("Please Enter valid Email");
            valid = false;
        } else if (password.isEmpty()) {
            ed_password.setError("Please Enter Password");
            valid = false;
        } else if (image == null) {
            error_stmt = "Please select a profile photo";
            Toast.makeText(this, error_stmt, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private void afterLoginFail(String errMsg) {
        loading.setVisibility(View.GONE);
        btn_regist.setVisibility(View.VISIBLE);
        getSound(this,0).start();
        Toast.makeText(RegisterActivity.this, "Error!!! " + errMsg, Toast.LENGTH_SHORT).show();
    }

    public void backToLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
