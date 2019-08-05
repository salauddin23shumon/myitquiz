package com.project.s1s1s1.myitquiz.utility;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.project.s1s1s1.myitquiz.R;
import com.project.s1s1s1.myitquiz.activity.MainActivity;

public class NotificationHelper extends ContextWrapper {
    private static final String CHANNEL_NAME = "notification";
    private static final int NOTIFICATION_ID = 440; // notification id
    private static final String CHANNEL_ID = "notf_chnl"; //channel id
    private static final int INTENT_RQST_CODE = 444;
    private NotificationManager manager;
    private String title;
    private String message;
    private Context context;

    public NotificationHelper(Context base, String title, String message) {
        super(base);
        context = base;
        this.title = title;
        this.message = message;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();    // if device version >= oreo
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);

    }

    public NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public void getNotification() {
        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, INTENT_RQST_CODE, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_announcement)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(NOTIFICATION_ID, notification.build());
    }

}
