package com.project.s1s1s1.myitquiz.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.project.s1s1s1.myitquiz.dataModel.Score;
import com.project.s1s1s1.myitquiz.dataModel.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.android.volley.VolleyLog.TAG;
import static com.project.s1s1s1.myitquiz.utility.Utils.getSound;
import static com.project.s1s1s1.myitquiz.utility.Utils.getStringImage;

public class VolleyResponse {
    private static final String URL_READ = "api_read_detail.php";
    public static final String UPDATE_PROFILE = "api_update_profile.php";
    private static NewObj newObj;

    public static void getUserDetailFromServer(final Context context, final User oldUser) {
        final UserPreference userPreference = new UserPreference(context);
        newObj = (NewObj) context;  // have to implement
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if (success.equals("1")) {
                                Log.e(TAG, "onResponse: " + success);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    final String strName = object.getString("username").trim();
                                    final String strEmail = object.getString("email").trim();
                                    String strPhoto = object.getString("photo");

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

                                    Score newScore = new Score(cpp, cs, cProg, java, html, ds, dld, css, js, computer, mysql, php);

                                    try {
                                        Bitmap bitmap = new BitmapAsyncTask().execute(strPhoto).get();
                                        User newUser = new User();
                                        newUser.setId(oldUser.getId());
                                        newUser.setPassword(oldUser.getPassword());
                                        newUser.setUserName(strName);
                                        newUser.setEmail(strEmail);
                                        newUser.setPhoto(getStringImage(bitmap));
                                        newUser.setUserScore(newScore);
                                        userPreference.saveUserData(newUser); //saving updated data
                                        newObj.newProfileUser(newUser); // interface calling

                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onResponse: " + errorResponse(error));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", oldUser.getId());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public interface NewObj {   ///have to implement this when using this class
        void newProfileUser(User user);
    }

    public static String errorResponse(VolleyError error) {

        Log.e(TAG, "errorResponse:msg " + error.getMessage());

        String message = error.getMessage();
        String showMsg = null;

        if (message != null && message.contains("Network is unreachable")) {
            showMsg = "Cannot connect to Internet...Please check your connection!";
        } else if (message != null && message.contains("Connection refused")) {
            showMsg = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof ServerError) {
            showMsg = "Server Side Error!";
        } else if (error instanceof NetworkError) {
            showMsg = "Network Error!";
        } else if (error instanceof ParseError) {
            showMsg = "Parsing Error!";
        } else if (error instanceof AuthFailureError) {
            showMsg = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof TimeoutError) {
            showMsg = "Connection TimeOut! Please try later.";
        }
        return showMsg;
    }


    public static void updateUser(final Context context, final User user) {

        Log.e(TAG, "updateUser: name-" + user.getUserName());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BASE_URL + UPDATE_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("success");

                            if (result.equals("1")) {
                                Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                getUserDetailFromServer(context, user);
                                getSound(context,1).start();
                            } else {///query error
                                Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                getUserDetailFromServer(context, user);
                                getSound(context,0).start();
                            }

                        } catch (JSONException e) { ///database or api or json internal error
                            e.printStackTrace();
                            getUserDetailFromServer(context, user);
                        }
                    }
                },
                new Response.ErrorListener() {////connection error
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + errorResponse(error));
                        getUserDetailFromServer(context, user);
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
