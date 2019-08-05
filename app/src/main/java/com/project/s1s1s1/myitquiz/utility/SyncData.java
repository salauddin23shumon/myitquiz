package com.project.s1s1s1.myitquiz.utility;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class SyncData {

    private static final int SYNC_JOB_ID = 420;
    private static final String TAG = "SyncData";
    private JobScheduler scheduler;
    private JobInfo info;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SyncData(Context context) {
        long interval = 15*60*1000;
//        long interval = 900000;
        scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName jobService = new ComponentName(context, SyncService.class);
        JobInfo.Builder builder = new JobInfo.Builder(SYNC_JOB_ID, jobService);
        builder.setPeriodic(interval);
        builder.setPersisted(true); /// trigger again when device is rebooted
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        info = builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void doSync() {
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void cancelJob(){
        scheduler.cancel(SYNC_JOB_ID);
    }
}
