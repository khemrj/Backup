package com.example.bloodlink.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bloodlink.R;
import com.example.bloodlink.databinding.ActivityDashboardBinding;
import com.example.bloodlink.donorpage.RecyclerDonorAdapter;
import com.example.bloodlink.donorpage.donorPage;
import com.example.bloodlink.myprofile.myprofile;
import com.example.bloodlink.requestedpage.requestlistpage;
import com.example.bloodlink.searchdonor.searchdonor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class dashboard extends AppCompatActivity {
ActivityDashboardBinding binding;
    ImageButton notify;
    Button requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setIdInSharedPreferences();
        requests=findViewById(R.id.requests);
        requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(dashboard.this, requestlistpage.class);
                startActivity(intent);
            }
        });


        notify = findViewById(R.id.notification);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(dashboard.this, donorPage.class);
                startActivity(i);
            }
        });

        ImageSlider imageSlider=binding.imageSlider;
        ArrayList<SlideModel>slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.bl1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.b2, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels,ScaleTypes.FIT);


        binding.requested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(dashboard.this, searchdonor.class);
                startActivity(intent);
            }
        });
       binding.person.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent=new Intent(dashboard.this, myprofile.class);
               startActivity(intent);
           }
       });

    }
    public void setIdInSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone",null);
        SharedPreferences sharedPreferencesurl = getSharedPreferences("url_prefs", Context.MODE_PRIVATE);
        String URL = sharedPreferencesurl.getString("URL", null);
        String url = URL +"/api/v1/user/getId/" + phone;


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonArrayRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userId",response);
                       editor.apply();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("errorResponse", "Error: " + error.toString());
                        // Handle error here
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest);

    }
    }
