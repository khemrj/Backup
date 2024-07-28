package com.example.bloodlink.requestedpage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodlink.StorageClass;
import com.example.bloodlink.donorpage.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiClient2 {
    public static Context getCtx() {
        return ctx;
    }

    private static ApiClient2 instance;
    private RequestQueue requestQueue;
    private static Context ctx;


    private ApiClient2(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiClient2 getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient2(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public interface VolleyCallback {
        void onSuccess(ArrayList<RequesterModel> result);
        void onError(VolleyError error);
    }


    public void getRequestors(final VolleyCallback callback) { //working one


        StorageClass s = new StorageClass();
        String requesterId = s.getRequesterId();
        Log.d("requesteridkkk", " "+requesterId);
        String url = "http://192.168.1.7:8085/api/requests"; // Replace with your actual API endpoint
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ApiClient2", "Response: " + response.toString()); // Log the response
                        ArrayList<RequesterModel> requestors = parseRequestorResponse(response);
                        callback.onSuccess(requestors);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                });

        getRequestQueue().add(jsonArrayRequest);
    }

    private ArrayList<RequesterModel> parseRequestorResponse(JSONArray response) {
        ArrayList<RequesterModel> requesterList = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                JSONObject requester = jsonObject.getJSONObject("requester");

                String name = requester.has("name") ? requester.getString("name") : "N/A";
                String bloodGroup = requester.has("bloodGroup") ? requester.getString("bloodGroup") : "N/A";
                int pints = requester.has("pints") ? requester.getInt("pints") : 0;
                double latitude = requester.has("latitude") ? requester.getDouble("latitude") : 0.0;
                double longitude = requester.has("longitude") ? requester.getDouble("longitude") : 0.0;

                // Create a model object or directly use these values to populate your UI
                RequesterModel requesterModel = new RequesterModel(name, bloodGroup, pints, latitude, longitude);
                requesterList.add(requesterModel);
            }
        } catch (JSONException e) {
            Log.e("ApiClient2", "JSON parsing error: ", e);
        }
        return requesterList;
    }

    private String calculateAge(String dateOfBirth) {
        return Utils.calculateAge(dateOfBirth);
    }
}
