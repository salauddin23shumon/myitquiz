/*
 * Copyright (c) 2019. $1$1$1. All Rights Reserved.
 *  <p>
 *  Save to the extent permitted by law, you may not use, copy, modify, distribute or create derivative works of this material  or any part of it without the prior written consent of $1$1$1.
 *  <p>
 *  The above Copyright notice  and the permission notice shall be included in all copies or substantial portions of the Software.
 */

package com.project.s1s1s1.myitquiz.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.project.s1s1s1.myitquiz.R;

import java.io.ByteArrayOutputStream;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static byte[] getImageBAO(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap getImageBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    public static Bitmap getBitmapImage(String image) {
        byte[] decodedByte = Base64.decode(image, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    public static void chooseImageFile(Context context, int requestCode) {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            ((Activity)context).startActivityForResult(Intent.createChooser(intent, "Select Photo"), requestCode);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }

    public static Animation setBtnAnim(){
        Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        return animation;
    }

    public static MediaPlayer getSound(Context context, int i) {

        if (i==1)
            return MediaPlayer.create(context, R.raw.succeess);
        else if (i==2)
            return MediaPlayer.create(context, R.raw.point_beep);
        else
            return MediaPlayer.create(context, R.raw.error_all);
    }

    public static MediaPlayer getMainTheme(Context context){
        MediaPlayer music=MediaPlayer.create(context, R.raw.piano_background);
        music.setLooping(true);
        return music;
    }
}
