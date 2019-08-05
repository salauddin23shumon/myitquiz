package com.project.s1s1s1.myitquiz.utility;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.s1s1s1.myitquiz.dataModel.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.s1s1s1.myitquiz.activity.ProfileActivity.oldUserData;
import static com.project.s1s1s1.myitquiz.utility.VolleyResponse.errorResponse;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SyncService extends JobService {

    private static final String TAG = "SyncService";


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "job start: ");
        syncData(jobParameters);
        return true;
    }

    private void syncData(final JobParameters jobParameters) {

        final PreferenceObject preferenceObject = new PreferenceObject(getApplicationContext());
        final User user = preferenceObject.getUserData();
        if (preferenceObject.getUserData() != null) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + VolleyResponse.UPDATE_PROFILE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("1")) {
                            user.setSync_status(Constant.SYNC_STATUS_OK);
                            preferenceObject.saveUserData(user);
//                            jobFinished(jobParameters, false);
                            new SyncData(getApplicationContext()).cancelJob();  // cancelling the job and also repetition
                            Log.d(TAG, "syncData: jobFinished successfully");
                        }else {
                            Log.d(TAG, "onResponse: "+jsonObject.getString("message"));
                            preferenceObject.saveUserData(oldUserData());
                            new SyncData(getApplicationContext()).cancelJob();  // cancelling the job and also repetition
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: " + errorResponse(error));
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", user.getId());
                    Log.e(TAG, "getParams: " + user.getId());
                    params.put("username", user.getUserName());
                    Log.e(TAG, "getParams: " + user.getUserName());
                    params.put("email", user.getEmail());
                    params.put("photo", user.getPhoto());

                    params.put("Computer", String.valueOf(user.getUserScore().getComputer()));
                    params.put("MySql", String.valueOf(user.getUserScore().getMySql()));
                    params.put("PHP", String.valueOf(user.getUserScore().getPhp()));
                    params.put("CSS", String.valueOf(user.getUserScore().getCss()));
                    params.put("C_prog", String.valueOf(user.getUserScore().getcProgramming()));
                    params.put("cSharp", String.valueOf(user.getUserScore().getcSharp()));
                    params.put("CPP", String.valueOf(user.getUserScore().getCpp()));
                    params.put("DS", String.valueOf(user.getUserScore().getDataStructure()));
                    params.put("JS", String.valueOf(user.getUserScore().getJavaScript()));
                    params.put("HTML", String.valueOf(user.getUserScore().getHtml()));
                    params.put("java", String.valueOf(user.getUserScore().getJava()));
                    params.put("DLD", String.valueOf(user.getUserScore().getDigitalLogic()));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        } else {
            Log.e(TAG, " no data into preference");

        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

}
