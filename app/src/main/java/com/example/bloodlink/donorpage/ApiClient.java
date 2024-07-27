package com.example.bloodlink.donorpage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class ApiClient {
    private static ApiClient instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private ApiClient(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
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
        void onSuccess(ArrayList<DonorModel> result);
        void onError(VolleyError error);
    }

    public void getDonors(final VolleyCallback callback) {

//        SharedPreferences sharedPreferencesurl = ctx.getSharedPreferences("url_prefs", Context.MODE_PRIVATE);
//        String URL = sharedPreferencesurl.getString("URL", null);
        String url = "http://192.168.18.7:8085/api/requests"; // Replace with your actual API endpoint
        Log.d("funct", "functionCalled");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<DonorModel> donors = parseResponse(response);
                        callback.onSuccess(donors);
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

    private ArrayList<DonorModel> parseResponse(JSONArray response) {
        ArrayList<DonorModel> donorModels = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                JSONObject requesterObj = jsonObject.getJSONObject("requester");
                JSONObject donorInfoObj = jsonObject.optJSONObject("donorInfo");

                // Extract information from requester
                String firstName = requesterObj.optString("name", "Unknown");
                String lastName = ""; // Last name is not present in the JSON, use empty string or handle accordingly
                String name = firstName + " " + lastName;

                // Extract age and blood group
                String bloodgroup = requesterObj.optString("bloodGroup", "Unknown");
                int pints = requesterObj.optInt("pints", 0);

                // Location
                String latitude = requesterObj.optString("latitude", "0");
                String longitude = requesterObj.optString("longitude", "0");
                String location = latitude + ", " + longitude;

                // Default age since no birth date information is available
                String age = "Unknown";

                DonorModel donorModel = new DonorModel(name, age, bloodgroup, pints, location);
                donorModels.add(donorModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.err.println("Error parsing JSON response: " + e.getMessage());
        }
        return donorModels;
    }



    private String calculateAge(String dateOfBirth) {
        String age = Utils.calculateAge(dateOfBirth);
        return age;
    }
}
