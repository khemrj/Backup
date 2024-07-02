package com.example.bloodlink.donorpage;

import android.content.Context;
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
        String url = "http://192.168.18.7:8085/api/requests"; // Replace with your actual API endpoint

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
                JSONObject donorInfoObj = jsonObject.getJSONObject("donorInfo");

                String name = donorInfoObj.getJSONObject("memberInfo").getString("firstname") +
                        " " + donorInfoObj.getJSONObject("memberInfo").getString("lastname");
                String age  = calculateAge(donorInfoObj.getJSONObject("memberInfo").getString("dateOfBirth"));
                String bloodgroup = donorInfoObj.getJSONObject("memberInfo").getString("bloodGroup");

                // Check if "pints" exists in the JSON object
                int pints = 0; // Default value if "pints" is missing
                if (donorInfoObj.has("pints")) {
                    pints = donorInfoObj.getInt("pints");
                }

                String location = requesterObj.getString("latitude") + ", " + requesterObj.getString("longitude");

                DonorModel donorModel = new DonorModel(name, age, bloodgroup, pints, location);
                donorModels.add(donorModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return donorModels;
    }


    private String calculateAge(String dateOfBirth) {
        String age = Utils.calculateAge(dateOfBirth);
        return age;

    }
}
