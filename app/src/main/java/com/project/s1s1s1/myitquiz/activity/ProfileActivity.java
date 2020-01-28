package com.project.s1s1s1.myitquiz.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.dataModel.User;
import com.project.s1s1s1.myitquiz.utility.Constant;
import com.project.s1s1s1.myitquiz.utility.SyncData;
import com.project.s1s1s1.myitquiz.utility.UserPreference;
import com.project.s1s1s1.myitquiz.utility.VolleyResponse;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.project.s1s1s1.myitquiz.utility.Utils.chooseImageFile;
import static com.project.s1s1s1.myitquiz.utility.Utils.getBitmapImage;
import static com.project.s1s1s1.myitquiz.utility.Utils.getSound;
import static com.project.s1s1s1.myitquiz.utility.Utils.getStringImage;

import static com.project.s1s1s1.myitquiz.utility.Utils.isNetworkAvailable;
import static com.project.s1s1s1.myitquiz.utility.VolleyResponse.updateUser;


public class ProfileActivity extends AppCompatActivity implements VolleyResponse.NewObj {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private static final int PHOTO_REQUEST_CODE = 1;
    private EditText ed_name, ed_email;
    private Button btn_photo_upload, btn_update, btn_edit;
    private String name, email, userImage, selectedImage = null, newImage, userID;
    private User user;
    private static User oldUser;        //copying previous value
    private ProgressDialog progressDialog;
    private CircleImageView profile_image;
    private UserPreference object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        object = new UserPreference(this);
        user = object.getUserData();
        oldUser = object.getUserData();           /// copying user data if offline update failed due query mismatch
        progressDialog = new ProgressDialog(this);

        ed_name = findViewById(R.id.name);
        ed_email = findViewById(R.id.email);
        btn_photo_upload = findViewById(R.id.btn_photo);
        btn_update = findViewById(R.id.btn_update);
        btn_edit = findViewById(R.id.btn_edit);
        profile_image = findViewById(R.id.profile_image);

        btn_photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageFile(ProfileActivity.this, PHOTO_REQUEST_CODE);  ///onActivityResult corresponding to this method
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btn_update_setting();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                SaveEditDetail();
            }
        });

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getUserDetails(user);

    }//end of onCreate

    ///set btn color and cursor
    private void btn_update_setting() {
        ed_name.setFocusableInTouchMode(true);
        ed_name.requestFocus();
        ed_email.setFocusableInTouchMode(true);
        ed_name.setSelection(ed_name.getText().length());
        ed_email.setSelection(ed_email.getText().length());
        btn_edit.setVisibility(View.INVISIBLE);
        btn_update.setVisibility(View.VISIBLE);
        btn_photo_upload.setVisibility(View.VISIBLE);
    }

    //UserDetails from sharedpreference
    private void getUserDetails(User user) {
        Bitmap userPic;
        btn_edit.setEnabled(true);
//        btn_edit.setBackgroundResource(R.drawable.glossy_btn_red);
        userImage = user.getPhoto();
        userPic = getBitmapImage(user.getPhoto());
        ed_name.setText(user.getUserName());
        ed_email.setText(user.getEmail());
        profile_image.setImageBitmap(userPic);
    }

    private void SaveEditDetail() {
        name = ed_name.getText().toString().trim();
        email = ed_email.getText().toString().trim();
        userID = user.getId();
        if (selectedImage != null) {
            newImage = selectedImage;
        } else {
            newImage = userImage;
        }

        if (validate()) {
            user.setUserName(name);
            user.setEmail(email);
            user.setPhoto(newImage);

            if (isNetworkAvailable(this)) {
                user.setSync_status(Constant.SYNC_STATUS_OK);
                updateUser(this, user);
            } else {
                user.setSync_status(Constant.SYNC_STATUS_FAILED);
                object.saveUserData(user);
                getSound(this, 2);
                Toast.makeText(this, "Updated data will be sync", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "SaveEditDetail: " + user.getSync_status());
            }
            btn_edit.setVisibility(View.VISIBLE);
            btn_update.setVisibility(View.INVISIBLE);
            btn_photo_upload.setVisibility(View.GONE);
            ed_email.setFocusable(false);
            ed_name.setFocusable(false);
            progressDialog.dismiss();

        } else {
            btn_update_setting();
        }
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
        }
        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                RequestOptions myOptions = new RequestOptions()
                        .centerCrop() // or centerCrop
                        .override(190, 190);    //resizing photo

                Glide
                        .with(ProfileActivity.this)
                        .asBitmap()
                        .apply(myOptions)
                        .load(bitmap)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                selectedImage = (getStringImage(resource));
                                profile_image.setImageBitmap(getBitmapImage(selectedImage));
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static User oldUserData() {
        return oldUser;
    }

    @Override
    public void newProfileUser(User modifiedUser) { // this interface is used to fetch updated User object from VolleyResponse class
        this.user = modifiedUser;
        getUserDetails(modifiedUser);
    }

    @Override
    protected void onStart() {
        super.onStart();

        int status = user.getSync_status();     // calling job scheduler to sync data
        Log.d(TAG, "onStart: status " + status);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (status == Constant.SYNC_STATUS_FAILED) {
                SyncData syncData = new SyncData(this);
                syncData.doSync();
            }
        }
    }
}
