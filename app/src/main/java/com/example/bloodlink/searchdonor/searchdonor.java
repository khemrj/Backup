package com.example.bloodlink.searchdonor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodlink.becomeadonor.becomeadonor;
import com.example.bloodlink.dashboard.dashboard;
import com.example.bloodlink.databinding.ActivitySearchdonorBinding;
import com.example.bloodlink.dlist;
import com.example.bloodlink.requestedpage.requestlistpage;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class searchdonor extends AppCompatActivity {

    ActivitySearchdonorBinding binding;
    ArrayList<String>arrbloodGroup=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchdonorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(binding.checkBox.isChecked()){
            binding.addressEditText.setEnabled(false);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        String phoneNo = sharedPreferences.getString("phone",null);
        binding.phone.setText(phoneNo);
        binding.patientNameContainer.setHelperText("");
        binding.pintContainer.setHelperText("");
        binding.addressContainer.setHelperText("");
        patientNameFocusListener();
        pintFocusListener();
        addressFocusListener();

        arrbloodGroup.add("A+");
        arrbloodGroup.add("AB+");
        arrbloodGroup.add("AB-");
        arrbloodGroup.add("B+");
        arrbloodGroup.add("B-");
        arrbloodGroup.add("O-");
        arrbloodGroup.add("O+");

        ArrayAdapter<String> bloodAdapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,arrbloodGroup);
        binding.bloodgroup.setAdapter(bloodAdapter);
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                RequestBlood();
                //geocode

//                if(binding.checkBox.isChecked())
//                {
//                    String s=binding.address.getText().toString();
//                }
//                else{
//                    String address=binding.address.getText().toString();
//                }
//                Intent intend=new Intent(searchdonor.this, dlist.class);
//                if(binding.checkBox.isChecked())
//                {
//                    String address=binding.address.getText().toString();
//                }
//                else{
//                    String address=binding.address.getText().toString();
//                }
                String patient = binding.patientNameEditText.getText().toString();
                String bloodgroup = binding.bloodgroup.getText().toString();
                String pint = binding.pintEditText.getText().toString();
                String s = binding.addressEditText.getText().toString();
                if (patient.isEmpty() && bloodgroup.isEmpty() && pint.isEmpty() && s.isEmpty()) {
                    Toast.makeText(searchdonor.this, "Please enter a field", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(searchdonor.this, dlist.class);
                    intent.putExtra("bloodgroup", bloodgroup);
                    intent.putExtra("pints", pint);
                    intent.putExtra("address", s);
                    startActivity(intent);
                }

            }

        });

    }
    private void patientNameFocusListener() {
        binding.patientNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                String result = validpatientName();
                if (result != null) {
                    binding.patientNameContainer.setHelperText(result);

                } else {
                    binding.patientNameContainer.setHelperText("");
                    // Clear error text if email is valid
                }
            }
        });
    }
    private String validpatientName() {
        String patientNameText = binding.patientNameEditText.getText().toString().trim();
        if (patientNameText.isEmpty()) {
            return "Patient name cannot be empty";
        }
        return null; // Return null if email is valid
    }
    private void pintFocusListener() {
        binding.pintEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = validPint();
                if (result != null) {
                    binding.pintContainer.setHelperText(result);

                } else {
                    binding.pintContainer.setHelperText("");
                    // Clear error text if email is valid
                }
            }
        });

    }

    private String validPint() {
        String pintNumber = binding.pintEditText.getText().toString().trim();
        if (pintNumber.length()>3) {
            return "Less than 3 pint is valid";
        }
        return null; // Return null if email is valid
    }
    private void addressFocusListener() {
        binding.addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                String result = validAddress();
                if (result != null) {
                    binding.addressContainer.setHelperText(result);

                } else {
                    binding.addressContainer.setHelperText("");
                    // Clear error text if email is valid
                }
            }
        });
    }
    private String validAddress() {
        String addressText = binding.addressEditText.getText().toString().trim();
        if (addressText.isEmpty()) {
            return "Please enter your address";
        }
        return null; // Return null if email is valid
    }
    public void RequestBlood() {

        GeoCodeLocation locationAddress = new GeoCodeLocation();
        locationAddress.getAddressFromLocation(binding.addressEditText.getText().toString(), getApplicationContext(), new GeoCoderHandler());

        SharedPreferences sharedPreferences = getSharedPreferences("url_prefs", Context.MODE_PRIVATE);
        String URL = sharedPreferences.getString("URL", null);
        String lat = sharedPreferences.getString("latitudeSearch",null);
        String lon = sharedPreferences.getString("longitudeSearch",null);
        String url = URL +"/api/v1/requesters";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("bloodGroup",binding.bloodgroup.getText());
            jsonRequest.put("latitude",lat);
            jsonRequest.put("longitude", lon);
            jsonRequest.put("phone", binding.phone.getText());
            jsonRequest.put("pints", binding.pintEditText.getText());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Intent intent = new Intent(searchdonor.this,dlist.class);
                // This Token has null value but why??
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("volleyError", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                Intent i = getIntent();
//                String Token = i.getStringExtra("Token");
                SharedPreferences sharedPreferences = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
                String Token = sharedPreferences.getString("AuthToken", null);
                Log.d("BeDonorTokeninheader",Token);

                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+Token);

                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            SharedPreferences sharedPreferences = getSharedPreferences("url_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String[] parts = locationAddress.split(" ");
            editor.putString("latitudeSearch",parts[0]);
            editor.putString("longitudeSearch",parts[1]);
            editor.apply();


            Log.d("Location1",locationAddress);

        }


    }
    public void saveRequest(){

    }
}



